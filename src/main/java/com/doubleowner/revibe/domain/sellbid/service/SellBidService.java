package com.doubleowner.revibe.domain.sellbid.service;

import com.doubleowner.revibe.domain.buybid.entity.BuyBid;
import com.doubleowner.revibe.domain.buybid.repository.BuyBidRepository;
import com.doubleowner.revibe.domain.execution.service.ExecutionService;
import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.repository.OptionRepository;
import com.doubleowner.revibe.domain.sellbid.dto.SellBidRequestDto;
import com.doubleowner.revibe.domain.sellbid.dto.SellBidResponseDto;
import com.doubleowner.revibe.domain.sellbid.entity.SellBid;
import com.doubleowner.revibe.domain.sellbid.repository.SellBidRepository;
import com.doubleowner.revibe.domain.user.entity.User;
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

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellBidService {

    private final BuyBidRepository buyBidRepository;
    private final SellBidRepository sellBidRepository;
    private final OptionRepository optionRepository;
    private final ExecutionService executionService;
    private final RedisTemplate<String, String> redisTemplate;

    //save시 redis에

    // 판매 입찰 생성
    @Transactional
    public SellBidResponseDto createSellBid(User loginUser, SellBidRequestDto requestDto) {
        Option option = optionRepository.findById(requestDto.getOptionId())
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, " 해당 옵션을 찾을 수 없습니다."));

        //판매입찰 객체 및 db 등록
        SellBid sellBid = requestDto.toEntity(option, loginUser);
        sellBidRepository.save(sellBid);

        //sorted set에 sell+optionId 키로 추가
        redisTemplate.opsForZSet().add("sell"+option.getId(),  sellBid.getId().toString(), sellBid.getPrice());

        //가장 높은 구매 입찰가 조회
        Set<ZSetOperations.TypedTuple<String>> highestScoreSet = redisTemplate.opsForZSet()
                .reverseRangeWithScores("buy" + option.getId(), 0, 0); // Get top element
        //가장 높은 가격, 가장 먼저 등록된 입찰가 객체를 초기화 하기 위한
        Double highestScore;
        String lowestValue = "";

        if (highestScoreSet != null && !highestScoreSet.isEmpty()) {
            ZSetOperations.TypedTuple<String> highestTuple = highestScoreSet.iterator().next();
            highestScore = highestTuple.getScore();

            //구매 입찰 중 가장 큰
            Set<String> valuesWithHighestScore = redisTemplate.opsForZSet()
                    .rangeByScore("buy" + option.getId(), highestScore, highestScore);

            //즉시구매가와 일치하지 않고 판매가가 더 높을 때 -> 판매 입찰 등록
            if(valuesWithHighestScore != null && highestScore < sellBid.getPrice()) {
                log.info("판매 입찰 등록됨");
            } //즉시구매가와 일치하지 않고 판매가가 더 낮을 때 -> 예외 처리
            else if(valuesWithHighestScore != null && highestScore > sellBid.getPrice()) {
                throw new CommonException(ErrorCode.BAD_REQUEST, "판매 입찰가는 즉시 구매가보다 높아야합니다.");
            }
            // 즉시 판매가와 일치할 경우
            else if (valuesWithHighestScore != null && !valuesWithHighestScore.isEmpty()) {
                //구매 입찰 id
                lowestValue = valuesWithHighestScore.iterator().next();
                Long buyBidId = Long.parseLong(lowestValue);

                //체결 생성
                executionService.createExecution(sellBid.getId(), buyBidId);

                // 입찰 상태 변경
                BuyBid buybid = buyBidRepository.findById(buyBidId).orElseThrow();
                buybid.delete();
                sellBid.delete();
                sellBidRepository.save(sellBid);
                buyBidRepository.save(buybid);

                //sorted set에서 value 삭제
                redisTemplate.opsForZSet().remove("sell"+option.getId(), sellBid.getId().toString());
                redisTemplate.opsForZSet().remove("buy"+option.getId(), lowestValue);
            }
        }

        //옵션 id가 같은 구매입찰이 없거나 판매가가 즉시 구매가보다 높을 시 판매 입찰 생성 후 리턴
        return SellBidResponseDto.toDto(sellBid);
    }

    // 판매 입찰 취소
    @Transactional
    public void deleteSellBid(User loginUser, Long sellBidId) {
        SellBid sellBid = sellBidRepository.findById(sellBidId)
                .orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE, ("해당 판매 요청을 찾을 수 없습니다.")));
        if(sellBid.getUser().equals(loginUser)) {
            throw new CommonException(ErrorCode.ILLEGAL_ARGUMENT, "사용자가 올바르지 않습니다.");
        }

        sellBid.delete();
        sellBidRepository.save(sellBid);
    }

    // 판매 입찰 조회
    @Transactional(readOnly = true)
    public Page<SellBidResponseDto> findAllBuyBid(User loginUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SellBid> sellBids = sellBidRepository.findByUserId(loginUser.getId(), pageable);

        return sellBids.map(SellBidResponseDto::toDto);
    }
}