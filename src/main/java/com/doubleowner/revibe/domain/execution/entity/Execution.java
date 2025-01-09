package com.doubleowner.revibe.domain.execution.entity;

import com.doubleowner.revibe.domain.payment.entity.Payment;
import com.doubleowner.revibe.domain.sellBid.entity.SellBid;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sellBid_id")
    private SellBid sell;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}