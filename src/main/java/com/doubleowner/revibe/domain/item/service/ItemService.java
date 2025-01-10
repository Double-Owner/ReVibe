package com.doubleowner.revibe.domain.item.service;

import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.domain.brand.repository.BrandRepository;
import com.doubleowner.revibe.domain.item.dto.request.ItemRequestDto;
import com.doubleowner.revibe.domain.item.dto.response.ItemResponseDto;
import com.doubleowner.revibe.domain.item.entity.Category;
import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final BrandRepository brandRepository;


    // 상품 등록
    public ItemResponseDto createItem(User loginUser, ItemRequestDto requestDto) {
        // 브랜드 찾기
        Brand brand = brandRepository.findByIdOrElseThrow(requestDto.getBrandId());

        // TODO S3 상품이미지 추가

        Item item = new Item(brand, requestDto.getName(), requestDto.getDescription(),
                Category.of(requestDto.getCategory()), requestDto.getImage(), loginUser);

        itemRepository.save(item);

        return ItemResponseDto.toDto(item);
    }
}