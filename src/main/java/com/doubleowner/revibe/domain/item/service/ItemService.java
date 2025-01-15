package com.doubleowner.revibe.domain.item.service;

import com.doubleowner.revibe.domain.brand.entity.Brand;
import com.doubleowner.revibe.domain.brand.repository.BrandRepository;
import com.doubleowner.revibe.domain.item.dto.request.ItemRequestDto;
import com.doubleowner.revibe.domain.item.dto.request.ItemUpdateRequestDto;
import com.doubleowner.revibe.domain.item.dto.response.ItemResponseDto;
import com.doubleowner.revibe.domain.item.entity.Category;
import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.ImageException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import com.doubleowner.revibe.global.exception.errorCode.ImageErrorCode;
import com.doubleowner.revibe.global.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final BrandRepository brandRepository;
    private final S3Uploader s3Uploader;


    // 상품 등록
    @Transactional
    public ItemResponseDto createItem(User loginUser, ItemRequestDto requestDto) {
        // 브랜드 찾기
        Brand brand = brandRepository.findByIdOrElseThrow(requestDto.getBrandId());
        String image = null;
        // 상품이미지 추가
        if(requestDto.getImage() != null) {
            try{
                image = s3Uploader.upload(requestDto.getImage());
            } catch (IOException e){
                throw new ImageException(ImageErrorCode.FAILED_UPLOAD_IMAGE);
            }
        }
        // 동일한 상품명이 이미 존재할 경우 예외처리
        if(itemRepository.existsByName(requestDto.getName())){
            throw new CommonException(ErrorCode.ALREADY_EXIST,"이미 존재하는 상품명 입니다.");
        }

        Item item = new Item(brand, requestDto.getName(), requestDto.getDescription(),
                Category.of(requestDto.getCategory()), image, loginUser);

        itemRepository.save(item);

        return ItemResponseDto.toDto(item);
    }

    // 상품 수정
    @Transactional
    public ItemResponseDto modifyItem(Long itemId, ItemUpdateRequestDto requestDto) {
        // 수정할 상품 찾기
        Item item = itemRepository.findByIdOrElseThrow(itemId);

        String image = item.getImage();

        if(requestDto.getImage() != null) {
            try {
                // 기존 이미지 삭제
                s3Uploader.deleteImage(item.getImage());
                // 새 이미지 업로드
                image = s3Uploader.upload(requestDto.getImage());
            } catch (IOException e) {
                throw new ImageException(ImageErrorCode.FAILED_UPLOAD_IMAGE);
            }
        }

        item.updateItem(requestDto,image);

        itemRepository.save(item);

        return ItemResponseDto.toDto(item);
    }

    // 상품 전체 조회
    public Page<ItemResponseDto> getAllItems(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemRepository.findAll(pageable);

        return items.map(ItemResponseDto::toDto);
    }

    // 상품 상세 조회
    public ItemResponseDto getItem(Long itemId) {
        Item item = itemRepository.findByIdOrElseThrow(itemId);

        return ItemResponseDto.toDto(item);
    }
}