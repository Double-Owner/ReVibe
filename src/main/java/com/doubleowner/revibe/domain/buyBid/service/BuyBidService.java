package com.doubleowner.revibe.domain.buyBid.service;

import com.doubleowner.revibe.domain.buyBid.dto.BuyBidRequestDto;
import com.doubleowner.revibe.domain.buyBid.dto.BuyBidResponseDto;
import com.doubleowner.revibe.domain.buyBid.entity.BuyBid;
import com.doubleowner.revibe.domain.buyBid.repository.BuyBidRepository;
import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.repository.OptionRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.global.common.enumType.BidStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BuyBidService {

    private final BuyBidRepository bidRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    //구매 입찰 생성
    @Transactional
    public BuyBidResponseDto createBuyBid(BuyBidRequestDto requestBody) {
        //유저 아이디 토큰으로 받아야함
        //User user = userRepository.findById(토큰값에 있는 Id).orElseThrow();
        //테스트용
        User user = userRepository.findById(1L).orElseThrow();
        Option option = optionRepository.findById(requestBody.getOptionId()).orElseThrow();

        //toEntity에 user 들어가야함
        BuyBid buyBid = bidRepository.save(requestBody.toEntity(option, user));

        return BuyBidResponseDto.toDto(buyBid);
    }

    //구매 입찰 제거 -> status 값 end로 변경
    @Transactional
    public void deleteBuyBid(Long buyBidId) {
        BuyBid buyBid = bidRepository.findById(buyBidId).orElseThrow();
        buyBid.delete();

        bidRepository.save(buyBid);
    }

    //1 구매 입찰 조회

    public Page<BuyBidResponseDto> findAllBuyBid(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BuyBid> buyBids = bidRepository.findByUserId(1L, pageable);

        return buyBids.map(BuyBidResponseDto::toDto);
    }
}
