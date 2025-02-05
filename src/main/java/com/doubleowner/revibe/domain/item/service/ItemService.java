package com.doubleowner.revibe.domain.item.service;

import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.domain.brand.repository.BrandRepository;
import com.doubleowner.revibe.domain.item.dto.request.ItemRequestDto;
import com.doubleowner.revibe.domain.item.dto.request.ItemUpdateRequestDto;
import com.doubleowner.revibe.domain.item.dto.response.ItemResponseDto;
import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.common.service.ImageService;
import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.ALREADY_EXIST;
import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;


@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final BrandRepository brandRepository;
    private final ImageService imageService;


    // 상품 등록
    @Transactional
    public ItemResponseDto createItem(User loginUser, ItemRequestDto requestDto) {
        // 브랜드 찾기
        Brand brand = brandRepository.findByName(requestDto.getBrandName());
        if (brand == null) {
            throw new CustomException(NOT_FOUND_VALUE);
        }

        // 동일한 상품명이 이미 존재할 경우 예외처리
        if (itemRepository.existsByName(requestDto.getName())) {
            throw new CustomException(ALREADY_EXIST);
        }

        String image = null;
        // 상품이미지 추가
        if (hasImage(requestDto.getImage())) {
            image = imageService.uploadImage(image, requestDto.getImage());
        }

        Item item = requestDto.toEntity(brand, image, loginUser);

        itemRepository.save(item);

        return ItemResponseDto.toDto(item);
    }

    // 상품 수정
    @Transactional
    public ItemResponseDto modifyItem(Long itemId, ItemUpdateRequestDto requestDto) {
        // 수정할 상품 찾기
        Item item = itemRepository.findByIdOrElseThrow(itemId);

        String image = item.getImage();

        if (hasImage(requestDto.getImage())) {
            image = imageService.uploadImage(image, requestDto.getImage());
            item.updateItem(requestDto, image);
            itemRepository.save(item);
            return ItemResponseDto.toDto(item);
        }

        item.updateItem(requestDto, image);

        itemRepository.save(item);

        return ItemResponseDto.toDto(item);
    }

    // 상품 전체 조회
    @Cacheable(cacheNames = "getItems", key = "'items:page:'+ #page+':size'+#size", cacheManager = "itemCacheManager", condition = "#page==1")
    public List<ItemResponseDto> getAllItems(int page, int size, String keyword, String brand) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Item> items = itemRepository.searchItems(pageable, keyword, brand);

        return items.map(ItemResponseDto::toDto).toList();
    }

    // 상품 상세 조회
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findByIdOrElseThrow(itemId);

        return ItemResponseDto.toDto(item);
    }

    private static boolean hasImage(MultipartFile multipartFile) {
        return multipartFile != null && !multipartFile.isEmpty();
    }
}