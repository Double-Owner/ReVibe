package com.doubleowner.revibe.domain.wishlist.service;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.domain.wishlist.dto.WishlistResponseDto;
import com.doubleowner.revibe.domain.wishlist.entity.Wishlist;
import com.doubleowner.revibe.domain.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String WISHLIST_COUNT_KEY_PREFIX = "item:wishlist:";

    // 관심상품등록, 해제 (좋아요 기능)
    @Transactional
    public boolean doWishlist(User loginUser, Long itemId) {
        String wishlistKey = WISHLIST_COUNT_KEY_PREFIX + itemId;

        if (Boolean.FALSE.equals(redisTemplate.hasKey(wishlistKey))) {
            ensureWishlistCached(loginUser);
        }

        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(wishlistKey, loginUser.getId().toString()))) {
            redisTemplate.opsForSet().remove(wishlistKey, loginUser.getId().toString());
            return false;
        }

        redisTemplate.opsForSet().add(wishlistKey, loginUser.getId().toString());
        return true;
    }

    // 관심상품 목록 조회
    public List<WishlistResponseDto> findWishlists(User loginUser) {
        List<WishlistResponseDto> wishlistDtos = getWishlistFromRedis(loginUser);

        if (!wishlistDtos.isEmpty()) {
            return wishlistDtos; // Redis에 데이터가 있다면 바로 반환
        }

        //  Redis에 데이터가 없으면 MySQL에서 조회 후 Redis에 저장
        List<Wishlist> wishlists = wishlistRepository.findByUser(loginUser);
        for (Wishlist wishlist : wishlists) {
            String key = WISHLIST_COUNT_KEY_PREFIX + wishlist.getItem().getId();
            redisTemplate.opsForSet().add(key, loginUser.getId().toString());
        }

        return wishlists.stream().map(WishlistResponseDto::toDto).toList();
    }

    // Redis에서 좋아요목록 가져오기
    private List<WishlistResponseDto> getWishlistFromRedis(User loginUser) {
        Set<String> itemKeys = redisTemplate.keys(WISHLIST_COUNT_KEY_PREFIX + "*");

        if (itemKeys == null || itemKeys.isEmpty()) {
            return List.of(); // Redis에 저장된 키가 없으면 빈 리스트 반환
        }

        List<WishlistResponseDto> wishlists = itemKeys.stream()
                .filter(key -> Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, loginUser.getId().toString())))
                .map(key -> {
                    Long itemId = Long.parseLong(key.replace(WISHLIST_COUNT_KEY_PREFIX, ""));
                    return new WishlistResponseDto(itemId); // 🚀 가벼운 DTO 변환
                })
                .toList();

        return wishlists;
    }

    // 스케줄러로 redis에있는 데이터 mysql에 저장
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void syncWishlistToMySQL() {
        Set<String> itemKeys = redisTemplate.keys(WISHLIST_COUNT_KEY_PREFIX + "*");

        if (itemKeys != null && !itemKeys.isEmpty()) {
            for (String wishlistKey : itemKeys) {
                Set<String> userIds = redisTemplate.opsForSet().members(wishlistKey);

                if (userIds != null && !userIds.isEmpty()) {
                    Long itemId = Long.parseLong(wishlistKey.replace(WISHLIST_COUNT_KEY_PREFIX, ""));
                    Item item = itemRepository.findByIdOrElseThrow(itemId);
                    item.setLikesCount((long) userIds.size());
                    itemRepository.save(item);

                    List<Wishlist> newWishlists = userIds.stream()
                            .map(userIdStr -> {
                                Long userId = Long.parseLong(userIdStr);
                                return wishlistRepository.existsByUserIdAndItemId(userId, itemId)
                                        ? null
                                        : new Wishlist(userRepository.findByIdOrElseThrow(userId), item);
                            })
                            .filter(wishlist -> wishlist != null)
                            .toList();

                    if (!newWishlists.isEmpty()) {
                        wishlistRepository.saveAll(newWishlists);
                    }
                }
            }
        }
        deleteWishlistFromMySQL();
    }

    // Redis에 없는 좋아요를 MySQL에서 삭제하는 메서드
    @Transactional
    public void deleteWishlistFromMySQL() {
        Set<String> itemKeys = redisTemplate.keys(WISHLIST_COUNT_KEY_PREFIX + "*");
        List<Long> existingItemIds = itemKeys.stream()
                .map(key -> Long.parseLong(key.replace(WISHLIST_COUNT_KEY_PREFIX, "")))
                .toList();

        List<Wishlist> wishlistsToDelete = wishlistRepository.findAllNotInItemIds(existingItemIds);
        wishlistRepository.deleteAll(wishlistsToDelete);
    }

    // 현재 사용자의 좋아요 목록(Mysql) 레디스에 저장
    private void ensureWishlistCached(User loginUser) {
        List<Wishlist> wishlists = wishlistRepository.findByUser(loginUser);

        for (Wishlist wishlist : wishlists) {
            String key = WISHLIST_COUNT_KEY_PREFIX + wishlist.getItem().getId();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                redisTemplate.opsForSet().add(key, loginUser.getId().toString());
            }
        }
    }
}
