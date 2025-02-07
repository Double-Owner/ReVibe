package com.doubleowner.revibe.domain.cart.service;

import com.doubleowner.revibe.domain.cart.dto.response.CartResponseDto;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.repository.OptionRepository;
import com.doubleowner.revibe.domain.user.entity.User;

import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartService {

    private final OptionRepository optionRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String CART_PREFIX = "cart:";

    // 장바구니 담기
    public CartResponseDto addCart(User loginUser,Long optionId) {
        String key = CART_PREFIX + loginUser.getId();

        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, optionId.toString()))){
            throw new CustomException(ErrorCode.ALREADY_EXIST);
        }

        Option option = optionRepository.findByIdOrElseThrow(optionId);

        // Redis 저장 (기본 TTL 설정 가능)
        redisTemplate.opsForSet().add(key, option.getId().toString());
        redisTemplate.expire(key, 7, TimeUnit.DAYS); // 7일 동안 유지

        return CartResponseDto.toDto(option);
    }

    // 내 장바구니 조회
    public List<CartResponseDto> getMyCarts(User loginUser) {
        String key = CART_PREFIX + loginUser.getId();

        // Redis에서 저장된 Option ID 목록 가져오기
        Set<String> optionIds = redisTemplate.opsForSet().members(key);

        // Option ID를 실제 Option 엔티티로 변환
        List<Option> options = optionRepository.findAllById(
                optionIds.stream().map(Long::valueOf).toList()
        );

        return options.stream().map(CartResponseDto::toDto).toList();
    }

    // 장바구니 상품 삭제
    @Transactional
    public void deleteCart(User loginUser, Long optionId) {
        String key = CART_PREFIX + loginUser.getId();
        if (Boolean.FALSE.equals(redisTemplate.opsForSet().isMember(key, optionId.toString()))){
            throw new CustomException(ErrorCode.NOT_FOUND_VALUE);
        }
        redisTemplate.opsForSet().remove(key, optionId.toString());

    }

}
