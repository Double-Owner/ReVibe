package com.doubleowner.revibe.domain.item.dto.request;

import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.domain.item.entity.Category;
import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ItemRequestDto {
    @NotNull(message = "브랜드 입력은 필수입니다")
    private String brandName;

    @NotBlank(message = "카테고리 입력은 필수입니다")
    private String category;

    @NotBlank(message = "상품이름 입력은 필수입니다")
    private String name;

    @NotBlank(message = "설명 입력은 필수입니다")
    private String description;

    @NotNull(message = "상품 사진은 필수입니다")
    private MultipartFile image;

    public Item toEntity(Brand brand, String image, User loginUser) {
        return Item.builder()
                .brand(brand)
                .image(image)
                .user(loginUser)
                .category(Category.of(this.category))
                .name(this.name)
                .description(this.description)
                .likeCount(0L)
                .build();
    }
}
