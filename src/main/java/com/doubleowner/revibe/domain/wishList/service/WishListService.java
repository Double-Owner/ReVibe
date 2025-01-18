package com.doubleowner.revibe.domain.wishList.service;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.wishList.dto.WishListResponseDto;
import com.doubleowner.revibe.domain.wishList.entity.WishList;
import com.doubleowner.revibe.domain.wishList.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ItemRepository itemRepository;

    // 관심상품등록 , 해제
    @Transactional
    public boolean doWishList(User loginUser, Long itemId) {
        if(wishListRepository.existsByUserIdAndItemId(loginUser.getId(),itemId)) {
            wishListRepository.deleteByUserIdAndItemId(loginUser.getId(),itemId);
            return false;
        }
        Item item = itemRepository.findByIdOrElseThrow(itemId);
        WishList wishList = new WishList(loginUser,item);

        wishListRepository.save(wishList);
        return true;
    }

    // 관심상품 목록 조회
    public List<WishListResponseDto> findWishLists(User loginUser){
        List<WishList> wishLists = wishListRepository.findByUser(loginUser);

        return wishLists.stream().map(WishListResponseDto::toDto).toList();
    }
}
