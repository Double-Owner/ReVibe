package com.doubleowner.revibe.domain.buyBid.repository;

import com.doubleowner.revibe.domain.buyBid.entity.BuyBid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BuyBidRepository extends JpaRepository<BuyBid, Long> {

    @Query("select bb from BuyBid bb where bb.user.id=:userId")
    Page<BuyBid> findByUserId(@Param("userId") Long userId, Pageable pageable);
}
