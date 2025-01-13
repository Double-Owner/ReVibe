package com.doubleowner.revibe.domain.cart.service;

import com.doubleowner.revibe.domain.cart.dto.request.CartRequestDto;
import com.doubleowner.revibe.domain.cart.dto.response.CartResponseDto;
import com.doubleowner.revibe.domain.cart.entity.Cart;
import com.doubleowner.revibe.domain.cart.repository.CartRepository;
import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final ItemRepository itemRepository;

    // 장바구니 담기
    public CartResponseDto addCart(User loginUser,CartRequestDto requestDto) {

        Item item = itemRepository.findByIdOrElseThrow(requestDto.getItemId());

        Cart cart = new Cart(loginUser, item);

        cartRepository.save(cart);

        return CartResponseDto.toDto(cart);
    }

    // 내 장바구니 조회
    public List<CartResponseDto> getMyCarts(User loginUser) {

        List<Cart> carts = cartRepository.findAllByUser(loginUser);

        return carts.stream().map(CartResponseDto::toDto).toList();
    }

    // 장바구니 상품 삭제
    @Transactional
    public void deleteCart(User loginUser, Long cartId) {
        Cart cart = cartRepository.findByIdAndUser(cartId, loginUser);
        cartRepository.delete(cart);
    }

}
