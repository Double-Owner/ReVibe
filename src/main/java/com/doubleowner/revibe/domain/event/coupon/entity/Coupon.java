package com.doubleowner.revibe.domain.event.coupon.entity;

import com.doubleowner.revibe.domain.event.coupon.dto.request.CouponRequestDto;
import com.doubleowner.revibe.domain.event.issuedCoupon.entity.IssuedCoupon;
import com.doubleowner.revibe.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssuedCoupon> issuedCoupons;

    public Coupon(long l, String firstComeFirstServed, int i, int i1) {
        this.name = firstComeFirstServed;
        this.price = i;
        this.totalQuantity = i1;
    }

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
            throw new IllegalArgumentException("쿠폰이 소진되었습니다.");
        }
    }
}
