package com.doubleowner.revibe.domain.buybid.repository;

import com.doubleowner.revibe.domain.buybid.entity.BuyBid;
import com.doubleowner.revibe.domain.option.entity.Option;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyBidRepository extends JpaRepository<BuyBid, Long> {

    @Query("select bb from BuyBid bb where bb.user.id=:userId")
    Page<BuyBid> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select bb from BuyBid bb where bb.option=:optionId order by bb.price desc, bb.createdAt desc" )
    List<BuyBid> findByOptionId(@Param("optionId") Long optionId);
}
