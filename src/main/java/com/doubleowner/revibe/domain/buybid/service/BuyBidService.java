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
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BuyBidService {

    private final BuyBidRepository buyBidRepository;
    private final OptionRepository optionRepository;
    private final SellBidRepository sellBidRepository;
    private final ExecutionService executionService;
    private final RedisTemplate<String, String> redisTemplate;

    //1 구매 입찰 생성
    @Transactional
    public BuyBidResponseDto createBuyBid(User loginUser, BuyBidRequestDto requestBody) {
        Option option = optionRepository.findById(requestBody.getOptionId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, "해당 옵션을 찾을 수 없습니다."));


        //BuyBid 객체 생성 및 DB에 등록
        BuyBid buyBid = requestBody.toEntity(option, loginUser);
        buyBidRepository.save(buyBid);

        //redis에 sorted set으로 buy + optionId로 키 추가
        redisTemplate.opsForZSet().add("buy" + option.getId(), buyBid.getId().toString(), buyBid.getPrice());

        Set<ZSetOperations.TypedTuple<String>> lowestScoreSet = redisTemplate.opsForZSet()
                .rangeWithScores("sell" + option.getId(), 0, 0);
        //가장 높은 가격, 가장 먼저 등록된 입찰
        Double lowestScore;
        String lowestValue;

        if (lowestScoreSet != null && !lowestScoreSet.isEmpty()) {
            ZSetOperations.TypedTuple<String> lowestTuple = lowestScoreSet.iterator().next();
            lowestScore = lowestTuple.getScore();

            Set<String> valuesWithLowestScore = redisTemplate.opsForZSet()
                    .rangeByScore("sell" + option.getId(), lowestScore, lowestScore);

            if (valuesWithLowestScore != null && lowestScore > buyBid.getPrice()) {
                log.info("구매 입찰 등록됨");
            } else if (valuesWithLowestScore != null && lowestScore < buyBid.getPrice()) {
                buyBidRepository.deleteById(buyBid.getId());
                throw new CommonException(ErrorCode.BAD_REQUEST, "구매 입찰가는 즉시 판매가보다 낮아야합니다.");
            }
            // 즉시 판매가와 일치할 경우
            else if (valuesWithLowestScore != null && !valuesWithLowestScore.isEmpty()) {
                //구매 입찰 id 및 객체 생성
                lowestValue = valuesWithLowestScore.iterator().next();
                Long sellBidId = Long.parseLong(lowestValue);
                SellBid sellBid = sellBidRepository.findById(sellBidId).orElseThrow();

                //유저가 자신의 물건을 사려고할 시
                if (sellBid.getUser().getId().equals(buyBid.getUser().getId())) {
                    throw new CommonException(ErrorCode.BAD_REQUEST, "자신의 물건은 구매할 수 없습니다.");
                }

                //체결 생성
                executionService.createExecution(sellBidId, buyBid.getId());

                //체결된 입찰 상태 변경
                buyBid.delete();
                sellBid.delete();
                sellBidRepository.save(sellBid);
                buyBidRepository.save(buyBid);

                //sorted set에서 해당 입찰 value 삭제
                redisTemplate.opsForZSet().remove("sell" + option.getId(), lowestValue);
                redisTemplate.opsForZSet().remove("buy" + option.getId(), buyBid.getId().toString());
            }
        }
        return BuyBidResponseDto.toDto(buyBid);
    }

    //1 구매 입찰 제거 -> status 값 end로 변경
    @Transactional
    public void deleteBuyBid(Long buyBidId) {
        BuyBid buyBid = buyBidRepository.findById(buyBidId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, "해당 구매입찰을 찾을 수 없습니다."));
        buyBid.delete();

        buyBidRepository.save(buyBid);
    }

    //1 구매 입찰 조회
    @Transactional(readOnly = true)
    public Page<BuyBidResponseDto> findAllBuyBid(User loginUser, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BuyBid> buyBids = buyBidRepository.findByUserId(loginUser.getId(), pageable);

        return buyBids.map(BuyBidResponseDto::toDto);
    }
}
