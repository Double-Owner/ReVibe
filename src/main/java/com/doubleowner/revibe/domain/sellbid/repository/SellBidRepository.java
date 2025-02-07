package com.doubleowner.revibe.domain.sellbid.repository;

import com.doubleowner.revibe.domain.sellbid.entity.SellBid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SellBidRepository extends JpaRepository<SellBid, Long> {
    @Query("select sb from SellBid sb where sb.user.id=:userId")
    Slice<SellBid> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select sb from SellBid sb where sb.options.id=:optionId and sb.status='ONPROGRESS'")
    Slice<SellBid> findByOptionId(@Param("optionId")Long optionId,Pageable pageable);
}
