package com.doubleowner.revibe.domain.option.repository;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.entity.Size;
import com.doubleowner.revibe.global.exception.CustomException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import static com.doubleowner.revibe.global.exception.errorCode.ErrorCode.NOT_FOUND_VALUE;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    boolean existsByItemIdAndSize(Long itemId, Size size);

    @EntityGraph(attributePaths = {"item"})
    default  Option findByIdOrElseThrow(Long optionId){
        return findById(optionId).orElseThrow(() -> new CustomException(NOT_FOUND_VALUE));
    }
}
