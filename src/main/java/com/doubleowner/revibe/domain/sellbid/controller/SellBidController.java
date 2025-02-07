package com.doubleowner.revibe.domain.sellbid.controller;

import com.doubleowner.revibe.domain.sellbid.dto.SellBidRequestDto;
import com.doubleowner.revibe.domain.sellbid.dto.SellBidResponseDto;
import com.doubleowner.revibe.domain.sellbid.service.SellBidService;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sell-bids")
@RequiredArgsConstructor
public class SellBidController {

    private final SellBidService sellBidService;
    /**
     *1 판매 입찰
     * @param requestDto - 판매입찰을 위한 요청 정보
     * @return responseDto - 판매입찰 완료 응답 dto
     */
    @PostMapping
    public CommonResponseBody<?> createSellBid(
            @RequestBody @Valid SellBidRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();
        SellBidResponseDto responseDto = sellBidService.createSellBid(loginUser, requestDto);

        return new CommonResponseBody<>("판매 입찰이 완료되었습니다", responseDto);
    }

    /**
     *1 판매 입찰 조회
     * @RequestParam page - 페이지 생성을 위한 요청
     * @RequestParam size - 페이지 사이즈를 위한 요청
     * @return  판매입찰 조회 응답 message
     */
    @GetMapping
    public CommonResponseBody<List<SellBidResponseDto>> findBuyBid(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        User loginUser = userDetails.getUser();
        List<SellBidResponseDto> sellBidResponseDtos = sellBidService.findAllBuyBid(loginUser, page, size);
        return new CommonResponseBody<>("현재 사용자 판매 입찰 내역입니다.",sellBidResponseDtos );
    }

    @GetMapping("/{optionId}")
    public CommonResponseBody<List<SellBidResponseDto>> findBuyBidByOptionId(
            @PathVariable Long optionId,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ){
        List<SellBidResponseDto> sellBidResponseDtos = sellBidService.findBuyBidByOptionId(optionId, page,size);
        return new CommonResponseBody<>("옵션에 대한 판매 입찰 내역입니다.", sellBidResponseDtos);
    }

    /**
     *1 판매 입찰 취소
     * @param sellBidId - 판매입찰 취소를 위한 요청 정보
     * @return  판매입찰 취소 응답 message
     */
    @DeleteMapping("/{sellBidId}")
    public CommonResponseBody<?> deleteSellBid(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long sellBidId) {

        User loginUser = userDetails.getUser();
        sellBidService.deleteSellBid(loginUser, sellBidId);

        return new CommonResponseBody<>("판매 입찰이 취소되었습니다.", null);
    }
}
