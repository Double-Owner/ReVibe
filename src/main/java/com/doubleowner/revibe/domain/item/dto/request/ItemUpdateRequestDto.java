package com.doubleowner.revibe.domain.item.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class ItemUpdateRequestDto {
    private String name;

    private String category;

    private String description;

    private MultipartFile image;
}
