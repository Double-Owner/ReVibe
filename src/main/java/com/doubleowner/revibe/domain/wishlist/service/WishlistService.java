package com.doubleowner.revibe.domain.wishlist.service;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.domain.wishlist.dto.WishlistResponseDto;
import com.doubleowner.revibe.domain.wishlist.entity.Wishlist;
import com.doubleowner.revibe.domain.wishlist.repository.WishlistRepository;
import jakarta.annotation.PostConstruct;
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
        // Redis에서 해당 아이템의 관심 여부 확인
        String wishlistKey = WISHLIST_COUNT_KEY_PREFIX + itemId;

        // 이미 관심상품 목록에 있으면 삭제 TODO 해당상품이 있는지 유효성 검사
        if (redisTemplate.opsForSet().isMember(wishlistKey, loginUser.getId().toString())) {

            redisTemplate.opsForSet().remove(wishlistKey, loginUser.getId().toString());
            return false;
        }
        // 관심상품 목록에 추가
        redisTemplate.opsForSet().add(wishlistKey, loginUser.getId().toString());
        return true;

    }

    // 관심상품 목록 조회
    public List<WishlistResponseDto> findWishlists(User loginUser){
        List<Wishlist> wishlists = wishlistRepository.findByUser(loginUser);

        return wishlists.stream().map(WishlistResponseDto::toDto).toList();
    }

    // 스케줄러로 redis에있는 데이터 mysql에 저장

    // Redis에서 좋아요 키가 있으면 MySQL에 저장, 없으면 삭제하는 메서드
    @Scheduled(fixedRate = 60000) // 일단 1분 마다 설정 ** TODO 상의 해서 결정
    public void syncWishlistToMySQL() {
        // Redis에서 모든 아이템 키를 가져온다.
        Set<String> itemKeys = redisTemplate.keys(WISHLIST_COUNT_KEY_PREFIX + "*");

        if (itemKeys != null && !itemKeys.isEmpty()) {
            for (String wishlistKey : itemKeys) {
                // Redis에서 해당 아이템에 대한 사용자 목록을 가져온다.
                Set<String> userIds = redisTemplate.opsForSet().members(wishlistKey);

                // 해당 아이템에 대한 사용자 목록을 MySQL에 저장
                if (userIds != null) {
                    for (String userIdString : userIds) {
                        Long itemId = Long.parseLong(wishlistKey.replace(WISHLIST_COUNT_KEY_PREFIX, ""));
                        Long userId = Long.parseLong(userIdString);

                        // MySQL에 저장된 wishlist가 없다면 추가
                        if (!wishlistRepository.existsByUserIdAndItemId(userId, itemId)) {
                            User user = userRepository.findByIdOrElseThrow(userId);
                            Item item = itemRepository.findByIdOrElseThrow(itemId);
                            Wishlist wishlist = new Wishlist(user, item);
                            wishlistRepository.save(wishlist);
                        }
                    }
                }
            }
        }

        // TODO 상품에 대한 좋아요 수 삽입 기능 추가 (count 쿼리사용)

        // Redis에 없는 아이템에 대해 MySQL에서 삭제하는 작업
        deleteWishlistFromMySQL();
    }

    // Redis에 없는 좋아요를 MySQL에서 삭제하는 메서드
    @Transactional
    public void deleteWishlistFromMySQL() {
        // MySQL에서 모든 wishlist 데이터를 조회
        List<Wishlist> wishlists = wishlistRepository.findAll();

        for (Wishlist wishlist : wishlists) {
            String wishlistKey = WISHLIST_COUNT_KEY_PREFIX + wishlist.getItem().getId();

            // Redis에서 해당 아이템의 사용자 목록에 없으면 삭제
            if (!redisTemplate.opsForSet().isMember(wishlistKey, wishlist.getUser().getId().toString())) {
                wishlistRepository.delete(wishlist);
            }
        }
    }



    // 애플리케이션 시작 시 MySQL에서 Redis로 데이터 로드
    @PostConstruct
    public void init() {

        initializeWishlistCache();
    }

    // MySQL에서 Redis로 데이터 로딩
    @Transactional
    public void initializeWishlistCache() {
        List<Wishlist> wishlists = wishlistRepository.findAll();
        for (Wishlist wishlist : wishlists) {
            String wishlistKey = WISHLIST_COUNT_KEY_PREFIX + wishlist.getItem().getId();
            redisTemplate.opsForSet().add(wishlistKey, wishlist.getUser().getId().toString());
        }

    }


}

