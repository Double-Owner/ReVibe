package com.doubleowner.revibe.domain.sellBid.controller;

import com.doubleowner.revibe.domain.buyBid.dto.BuyBidResponseDto;
import com.doubleowner.revibe.domain.sellBid.dto.SellBidRequestDto;
import com.doubleowner.revibe.domain.sellBid.dto.SellBidResponseDto;
import com.doubleowner.revibe.domain.sellBid.service.SellBidService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sell-bids")
@RequiredArgsConstructor
public class SellBidController {

    private final SellBidService sellBidService;
    /**
     *1 판매 입찰
     * @param requestDto - 판매입찰을 위한 요청 정보
     * @return responseDto - 판매입찰 완료 응답 dto
     */
    @PostMapping
    public CommonResponseBody<?> createSellBid(@RequestBody @Valid SellBidRequestDto requestDto) {

        SellBidResponseDto responseDto = sellBidService.createSellBid(requestDto);

        return new CommonResponseBody<>("판매 입찰이 완료되었습니다", responseDto);
    }

    /**
     *1 판매 입찰 조회
     * @RequestParam page - 페이지 생성을 위한 요청
     * @RequestParam size - 페이지 사이즈를 위한 요청
     * @return  판매입찰 조회 응답 message
     */
    @GetMapping
    public CommonResponseBody<Page<SellBidResponseDto>> findBuyBid(
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        Page<SellBidResponseDto> sellBidResponseDtos = sellBidService.findAllBuyBid(page, size);
        return new CommonResponseBody<>("사용자 구매 입찰 내역입니다.",sellBidResponseDtos );
    }

    /**
     *1 판매 입찰 취소
     * @param sellBidId - 판매입찰 취소를 위한 요청 정보
     * @return  판매입찰 취소 응답 message
     */
    @DeleteMapping("/{sellBidId}")
    public CommonResponseBody<?> deleteSellBid(@PathVariable Long sellBidId) {

        sellBidService.deleteSellBid(sellBidId);

        return new CommonResponseBody<>("판매 입찰이 취소되었습니다.", null);
    }

}
