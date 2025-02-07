package com.doubleowner.revibe.domain.option.service;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.option.dto.request.OptionRequestDto;
import com.doubleowner.revibe.domain.option.dto.response.OptionResponseDto;
import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.entity.Size;
import com.doubleowner.revibe.domain.option.repository.OptionRepository;
import com.doubleowner.revibe.global.exception.CustomException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.ALREADY_EXIST_SIZE;

@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionRepository optionRepository;

    private final ItemRepository itemRepository;

    // 옵션 등록
    public OptionResponseDto createOption(Long itemId, OptionRequestDto requestDto) {
        Item item = itemRepository.findByIdOrElseThrow(itemId);

        //이미 같은 사이즈 있을 때 예외처리
        if(optionRepository.existsByItemIdAndSize(itemId,Size.of(requestDto.getSize()))){
            throw new CustomException(ALREADY_EXIST_SIZE);
        }

        Option option = new Option(Size.of(requestDto.getSize()), item);

        optionRepository.save(option);

        return OptionResponseDto.toDto(option);
    }

}
