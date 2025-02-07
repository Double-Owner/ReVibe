package com.doubleowner.revibe.domain.execution.contoller;

import com.doubleowner.revibe.domain.execution.dto.ExecutionResponseDto;
import com.doubleowner.revibe.domain.execution.service.ExecutionService;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.dto.CommonResponseBody;
import com.doubleowner.revibe.global.config.auth.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/executions")
public class ExecutionController {
    private final ExecutionService executionService;

    //todo 마감 기한에 맞게 체결 생성되게
    @PostMapping
    public ResponseEntity<ExecutionResponseDto> createExecution(@RequestParam Long sellBidId, @RequestParam Long buyBidId) {
        ExecutionResponseDto execution = executionService.createExecution(sellBidId, buyBidId);
        return ResponseEntity.ok(execution);
    }

    @GetMapping
    public CommonResponseBody<List<ExecutionResponseDto>> findExecution(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) {
        List<ExecutionResponseDto> execution = executionService.findExecution(page, size);
        return new CommonResponseBody<>("체결 내역 조회입니다.", execution);
    }
}
