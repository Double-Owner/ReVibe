package com.doubleowner.revibe.domain.option.entity;

import com.doubleowner.revibe.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "OPTIONS")
@NoArgsConstructor
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Size size;

    //상품 옵션 사이즈 별 갯수
    @Column(nullable = false)
    private Long stock;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public Option(Size size, Item item) {
        this.size = size;
        this.stock = 0L;
        this.item = item;
    }

    public void increaseStrock() {
        this.stock ++;
    }

    public void decreaseStrock() {
        this.stock--;
    }
}
