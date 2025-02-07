package com.doubleowner.revibe.domain.item.entity;

import com.doubleowner.revibe.domain.item.dto.request.ItemUpdateRequestDto;
import com.doubleowner.revibe.global.common.BaseTimeEntity;
import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Builder
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

    private String image;

    @ColumnDefault("0")
    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User user;

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

    public void setLikesCount(Long likesCount) {
        this.likeCount = likesCount;
    }
}