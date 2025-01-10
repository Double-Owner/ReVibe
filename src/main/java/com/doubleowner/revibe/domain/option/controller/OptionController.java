package com.doubleowner.revibe.domain.option.controller;

import com.doubleowner.revibe.domain.option.dto.request.OptionRequestDto;
import com.doubleowner.revibe.domain.option.dto.response.OptionResponseDto;
import com.doubleowner.revibe.domain.option.service.OptionService;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class OptionController {

    private final OptionService optionService;

    // 옵션 등록
    @PostMapping
    @RequestMapping("/{itemId}/options")
    public ResponseEntity<CommonResponseBody<OptionResponseDto>> createOption(
            @PathVariable Long itemId,
            @Valid @RequestBody OptionRequestDto requestDto)
    {
        OptionResponseDto optionResponseDto = optionService.createOption(itemId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseBody<>("상품에 옵션을 등록했습니다.",optionResponseDto));

    }

}
