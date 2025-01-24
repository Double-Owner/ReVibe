package com.doubleowner.revibe.domain.event.coupon.service;

import com.doubleowner.revibe.domain.event.coupon.dto.request.CouponRequestDto;
import com.doubleowner.revibe.domain.event.coupon.dto.response.CouponResponseDto;
import com.doubleowner.revibe.domain.event.coupon.entity.Coupon;
import com.doubleowner.revibe.domain.event.coupon.repository.CouponRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponResponseDto createCoupon(User user, CouponRequestDto dto) {

        Coupon coupon = Coupon.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .totalQuantity(dto.getTotalQuantity())
                .issuedStart(dto.getIssuedStart())
                .issuedEnd(dto.getIssuedEnd())
                .build();

        Coupon savedCoupon = couponRepository.save(coupon);

        return toDto(savedCoupon);
    }

    @Transactional
    public List<CouponResponseDto> findCoupons(User user, int page, int size) {

        Pageable pageable = PageRequest.of(page -1, size);
        List<Coupon> coupons = couponRepository.findAll(pageable).stream().toList();

        return coupons.stream().map(this::toDto).toList();
    }

    public CouponResponseDto updateCoupon(User user,Long id, CouponRequestDto dto) {

        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        coupon.updateCoupon(dto);
        return toDto(coupon);

    }

    public void deleteCoupon(User user, Long id) {

        Coupon coupon = couponRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("쿠폰을 찾을 수 없습니다."));

        couponRepository.deleteById(coupon.getId());
    }

    /**
     * toDto 메서드
     */
    private CouponResponseDto toDto(Coupon coupon) {
        return CouponResponseDto.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .price(coupon.getPrice())
                .totalQuantity(coupon.getTotalQuantity())
                .issuedStart(coupon.getIssuedStart())
                .issuedEnd(coupon.getIssuedEnd())
                .build();
    }

}
