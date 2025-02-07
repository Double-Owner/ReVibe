package com.doubleowner.revibe.domain.buybid.service;

import com.doubleowner.revibe.domain.buybid.dto.BuyBidRequestDto;
import com.doubleowner.revibe.domain.buybid.dto.BuyBidResponseDto;
import com.doubleowner.revibe.domain.buybid.entity.BuyBid;
import com.doubleowner.revibe.domain.buybid.repository.BuyBidRepository;
import com.doubleowner.revibe.domain.execution.service.ExecutionService;
import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.repository.OptionRepository;
import com.doubleowner.revibe.domain.sellbid.entity.SellBid;
import com.doubleowner.revibe.domain.sellbid.repository.SellBidRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.aop.DistributedLock;
import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyBidService {

    private final BuyBidRepository buyBidRepository;
    private final OptionRepository optionRepository;
    private final SellBidRepository sellBidRepository;
    private final ExecutionService executionService;
    private final RedisTemplate<String, String> redisTemplate;

    // 구매 입찰 생성
    @Transactional
    public BuyBidResponseDto createBuyBid(User loginUser, BuyBidRequestDto requestDto) {
        Option option = optionRepository.findById(requestDto.getOptionId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));


        //BuyBid 객체 생성 및 DB에 등록
        BuyBid buyBid = requestDto.toEntity(option, loginUser);
        buyBidRepository.save(buyBid);

        //redis에 sorted set으로 buy + optionId로 키 추가
        double score = buyBid.getPrice() + (System.currentTimeMillis() * 0.0000000000001);
        redisTemplate.opsForZSet().add("buy" + option.getId(), buyBid.getId().toString(), score);

        //가장 낮은 판매 입찰 가져오기 -> 없으면 입찰 생성 한 상태로 종료
        Set<ZSetOperations.TypedTuple<String>> lowestScoreSet = redisTemplate.opsForZSet()
                .rangeWithScores("sell" + option.getId(), 0, 0);

        if (lowestScoreSet != null && !lowestScoreSet.isEmpty()) {
            ZSetOperations.TypedTuple<String> lowestTuple = lowestScoreSet.iterator().next();
            double lowestScore = lowestTuple.getScore();

            //판매 입찰 중 가장 낮은
            Set<String> valuesWithLowestScore = redisTemplate.opsForZSet()
                    .rangeByScore("sell" + option.getId(), lowestScore, lowestScore);

            if (valuesWithLowestScore != null) {
                //즉시판매가와 일치하지 않고 구매가가 더 낮을 때 -> 구매 입찰 등록
                if ((long)lowestScore > buyBid.getPrice()) {
                    log.info("구매 입찰 등록됨");
                }
                //즉시판매가와 일치하지 않고 구매가가 더 높을 때 -> 예외 처리 sorted set에서도 삭제
                else if ((long)lowestScore < buyBid.getPrice()) {
                    redisTemplate.opsForZSet().remove("buy" + option.getId(), buyBid.getId().toString());
                    buyBidRepository.deleteById(buyBid.getId());
                    throw new CustomException(ErrorCode.INVALID_BID_PRICE);
                }
                // 즉시 판매가와 일치할 경우
                else if (!valuesWithLowestScore.isEmpty()) {
                    //구매 입찰 id 및 객체 생성
                    String lowestValue = valuesWithLowestScore.iterator().next();
                    Long sellBidId = Long.parseLong(lowestValue);
                    SellBid sellBid = sellBidRepository.findById(sellBidId).orElse(null);
                    if (sellBid == null) {
                        log.error("SellBid ID {}를 찾을 수 없습니다.", sellBidId);
                        log.info("입찰 요청: User={}, OptionId={}, Price={}",
                                loginUser.getId(), requestDto.getOptionId(), requestDto.getPrice());
                        redisTemplate.opsForZSet().remove("sell" + option.getId(), lowestValue);
                        throw new CustomException(ErrorCode.NOT_FOUND_VALUE);
                    }

                    //유저가 자신의 물건을 사려고할 시
                    if (sellBid.getUser().getId().equals(buyBid.getUser().getId())) {
                        redisTemplate.opsForZSet().remove("buy" + option.getId(), buyBid.getId().toString());
                        throw new CustomException(CANNOT_PURCHASE_OWN_ITEM);
                    }

                    //sorted set에서 해당 입찰 value 삭제
                    redisTemplate.opsForZSet().remove("buy" + option.getId(), buyBid.getId().toString());
                    redisTemplate.opsForZSet().remove("sell" + option.getId(), lowestValue);

                    //입찰 상태 변경
                    sellBid.delete();
                    buyBid.delete();
                    decreaseOptionStocks(option);

                    //체결 생성
                    createExecution(sellBidId, buyBid.getId());

                }
            }
        }
        return BuyBidResponseDto.toDto(buyBid);
    }

    // 구매 입찰 제거 -> status 값 end로 변경
    @Transactional
    public void deleteBuyBid(Long buyBidId) {
        BuyBid buyBid = buyBidRepository.findById(buyBidId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));
        buyBid.delete();
    }

    // 구매 입찰 조회
    @Transactional(readOnly = true)
    public List<BuyBidResponseDto> findAllBuyBid(User loginUser, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Slice<BuyBid> buyBids = buyBidRepository.findByUserId(loginUser.getId(), pageable);

        return buyBids.map(BuyBidResponseDto::toDto).toList();
    }

    public List<BuyBidResponseDto> findBuyBidByOptionId(Long optionId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<BuyBid> buyBids = buyBidRepository.findByOptionId(optionId, pageable);

        return buyBids.map(BuyBidResponseDto::toDto).toList();
    }

    @Transactional
    @DistributedLock(key = "#option.id")
    public void decreaseOptionStocks(Option option) {
        option.decreaseStrock();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createExecution(Long sellBidId, Long buyBidId) {
        executionService.createExecution(sellBidId, buyBidId);
    }
}
