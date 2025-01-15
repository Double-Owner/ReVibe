package com.doubleowner.revibe.domain.option.repository;

import com.doubleowner.revibe.domain.option.entity.Option;
import com.doubleowner.revibe.domain.option.entity.Size;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    boolean existsByItemIdAndSize(Long itemId, Size size);
}
