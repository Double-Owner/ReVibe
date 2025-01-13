package com.doubleowner.revibe.domain.sellBid.service;

import com.doubleowner.revibe.domain.buyBid.dto.BuyBidResponseDto;
import com.doubleowner.revibe.domain.buyBid.entity.BuyBid;
import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.repository.OptionRepository;
import com.doubleowner.revibe.domain.sellBid.dto.SellBidRequestDto;
import com.doubleowner.revibe.domain.sellBid.dto.SellBidResponseDto;
import com.doubleowner.revibe.domain.sellBid.entity.SellBid;
import com.doubleowner.revibe.domain.sellBid.repository.SellBidRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SellBidService {

    private final SellBidRepository sellBidRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;

    //1 판매 입찰 생성
    @Transactional
    public SellBidResponseDto createSellBid(User loginUser ,SellBidRequestDto requestDto) {
        Option option = optionRepository.findById(requestDto.getOptionId()).orElseThrow(() -> new IllegalArgumentException(""));
        SellBid sellBid = sellBidRepository.save(requestDto.toEntity(option, loginUser));

        return SellBidResponseDto.toDto(sellBid);
    }

    //1 판매 입찰 취소
    @Transactional
    public void deleteSellBid(User loginUser, Long sellBidId) {
        SellBid sellBid = sellBidRepository.findById(sellBidId).orElseThrow();
        if(sellBid.getUser().equals(loginUser)) {
            throw new IllegalArgumentException("사용자가 올바르지 않습니다.");
        };

        sellBid.delete();
        sellBidRepository.save(sellBid);
    }

    //1 판매 입찰 조회
    @Transactional(readOnly = true)
    public Page<SellBidResponseDto> findAllBuyBid(User loginUser, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SellBid> sellBids = sellBidRepository.findByUserId(loginUser.getId(), pageable);

        return sellBids.map(SellBidResponseDto::toDto);
    }
}
