package com.doubleowner.revibe.domain.wishlist.service;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.wishlist.dto.WishlistResponseDto;
import com.doubleowner.revibe.domain.wishlist.entity.Wishlist;
import com.doubleowner.revibe.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ItemRepository itemRepository;

    // 관심상품등록 , 해제
    @Transactional
    public boolean doWishlist(User loginUser, Long itemId) {
        if(wishlistRepository.existsByUserIdAndItemId(loginUser.getId(),itemId)) {
            wishlistRepository.deleteByUserIdAndItemId(loginUser.getId(),itemId);
            return false;
        }
        Item item = itemRepository.findByIdOrElseThrow(itemId);
        Wishlist wishlist = new Wishlist(loginUser,item);

        wishlistRepository.save(wishlist);
        return true;
    }

    // 관심상품 목록 조회
    public List<WishlistResponseDto> findWishlists(User loginUser){
        List<Wishlist> wishlists = wishlistRepository.findByUser(loginUser);

        return wishlists.stream().map(WishlistResponseDto::toDto).toList();
    }
}
