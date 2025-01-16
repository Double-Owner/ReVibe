package com.doubleowner.revibe.domain.option.repository;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.entity.Size;
import com.doubleowner.revibe.global.exception.CommonException;
import com.doubleowner.revibe.global.exception.errorCode.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    boolean existsByItemIdAndSize(Long itemId, Size size);

    default Option findByIdOrElseThrow(Long optionId){
        return findById(optionId).orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_VALUE,"해당 상품의 옵션을 찾을 수 없습니다."));
    }
}
