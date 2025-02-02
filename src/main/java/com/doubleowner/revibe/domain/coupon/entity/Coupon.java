package com.doubleowner.revibe.domain.coupon.entity;

import com.doubleowner.revibe.domain.coupon.dto.request.CouponRequestDto;
import com.doubleowner.revibe.global.common.BaseTimeEntity;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coupon extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int totalQuantity;

    @Column(nullable = true)
    private LocalDateTime issuedStart;

    @Column(nullable = true)
    private LocalDateTime issuedEnd;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssuedCoupon> issuedCoupons;

    public void updateCoupon(CouponRequestDto dto) {
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.totalQuantity = dto.getTotalQuantity();
        this.issuedStart = dto.getIssuedStart();
        this.issuedEnd = dto.getIssuedEnd();
    }

    public void decrementCoupon() {
        if(this.totalQuantity > 0) {
            this.totalQuantity--;
        } else {
            throw new CommonException(ErrorCode.GENERATE_MORE_THAN_MAX_COUNT);
        }
    }
}
