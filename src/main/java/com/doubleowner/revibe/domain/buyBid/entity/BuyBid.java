package com.doubleowner.revibe.domain.buyBid.entity;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.enumType.BidStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BidStatus buyStatus;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    //softDelete
    public void delete(){
        this.buyStatus = BidStatus.END;
    }

}
