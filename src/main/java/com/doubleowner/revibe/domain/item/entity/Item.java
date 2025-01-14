package com.doubleowner.revibe.domain.item.entity;

import com.doubleowner.revibe.domain.item.dto.request.ItemUpdateRequestDto;
import com.doubleowner.revibe.global.common.BaseTimeEntity;
import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User user;

    public Item(Brand brand, String name, String description, Category category, String image, User user) {
        this.brand = brand;
        this.name = name;
        this.description = description;
        this.category = category;
        this.image = image;
        this.user = user;
    }

    public void updateItem(ItemUpdateRequestDto requestDto,String image) {
        if(requestDto.getName() != null && !requestDto.getName().isEmpty()) {
            this.name = requestDto.getName();
        }
        if(requestDto.getCategory() != null && !requestDto.getCategory().isEmpty()) {
            this.category = Category.of(requestDto.getCategory());
        }

        if(requestDto.getDescription() != null && !requestDto.getDescription().isEmpty()) {
            this.description = requestDto.getDescription();
        }
        this.image = image;
    }
}