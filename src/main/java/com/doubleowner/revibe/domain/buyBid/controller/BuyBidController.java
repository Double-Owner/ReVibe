package com.doubleowner.revibe.domain.buyBid.controller;

import com.doubleowner.revibe.domain.buyBid.dto.BuyBidRequestDto;
import com.doubleowner.revibe.domain.buyBid.dto.BuyBidResponseDto;
import com.doubleowner.revibe.domain.buyBid.service.BuyBidService;
import com.doubleowner.revibe.domain.user.dto.response.UserSignupResponseDto;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buy-bids")
@RequiredArgsConstructor
public class BuyBidController {

    private final BuyBidService bidService;
    /**
     *1 구매 입찰
     * @param requestDto - 구매입찰을 위한 요청 정보
     * @return responseDto - 구매입찰 완료 응답 dto
     */
    @PostMapping
    public CommonResponseBody<?> createBuyBid(@RequestBody @Valid  BuyBidRequestDto requestDto) {

        BuyBidResponseDto responseDto = bidService.createBuyBid(requestDto);

        return new CommonResponseBody<>("구매 등록이 완료되었습니다." , responseDto);
    }

    /**
     *1 구매 입찰 조회
     * @RequestParam page - 페이지 생성을 위한 요청
     * @RequestParam size - 페이지 사이즈를 위한 요청
     * @return  구매입찰 조회 응답 message
     */
    @GetMapping
    public CommonResponseBody<Page<BuyBidResponseDto>> findBuyBid(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        Page<BuyBidResponseDto> buyBidResponseDtos = bidService.findAllBuyBid(page, size);
        return new CommonResponseBody<>("사용자 구매 입찰 내역입니다.",buyBidResponseDtos );
    }

    /**
     *1 구매 입찰 취소
     * @param buyBidId - 구매입찰 취소를 위한 요청 정보
     * @return  구매입찰 취소 응답 message
     */
    @DeleteMapping("/{buyBidId}/refund")
    public CommonResponseBody<?> deleteBuyBid(@PathVariable Long buyBidId){
        bidService.deleteBuyBid(buyBidId);

        return new CommonResponseBody<>("구매 입찰이 취소되었습니다.", null);
    }


}
