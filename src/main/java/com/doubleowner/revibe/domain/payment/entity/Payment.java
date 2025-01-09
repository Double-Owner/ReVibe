package com.doubleowner.revibe.domain.payment.entity;

import com.doubleowner.revibe.domain.buyBid.entity.BuyBid;
import com.doubleowner.revibe.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PayMethod payMethod;

    @Enumerated(value = EnumType.STRING)
    private PayStatus payStatus;

    @OneToOne
    @JoinColumn(name = "buyBid_id")
    private BuyBid buy;
}
