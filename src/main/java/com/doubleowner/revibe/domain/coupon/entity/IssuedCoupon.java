package com.doubleowner.revibe.domain.coupon.entity;

import com.doubleowner.revibe.domain.coupon.dto.response.IssuedCouponResponseDto;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus status = CouponStatus.ACTIVE;

    @Column(nullable = true)
    private LocalDateTime usedAt;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void usedCoupon() {
        if (this.status == CouponStatus.USED) {
            throw new CommonException(ErrorCode.ALREADY_USED_COUPON);
        }
        this.status = CouponStatus.USED;
        this.usedAt = LocalDateTime.now();
    }

    public static IssuedCouponResponseDto toDto(IssuedCoupon issuedCoupon) {
        return IssuedCouponResponseDto.builder()
                .issuedCouponId(issuedCoupon.getId())
                .couponId(issuedCoupon.coupon.getId())
                .userId(issuedCoupon.user.getId())
                .couponName(issuedCoupon.getCoupon().getName())
                .status(issuedCoupon.getStatus())
                .usedAt(issuedCoupon.getUsedAt())
                .createdAt(issuedCoupon.getCreatedAt())
                .build();
    }

}
