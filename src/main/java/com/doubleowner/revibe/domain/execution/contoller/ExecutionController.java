package com.doubleowner.revibe.domain.execution.contoller;

import com.doubleowner.revibe.domain.execution.dto.ExecutionResponseDto;
import com.doubleowner.revibe.domain.execution.service.ExecutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
