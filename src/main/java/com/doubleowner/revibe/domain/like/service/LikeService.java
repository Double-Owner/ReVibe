package com.doubleowner.revibe.domain.like.service;

import com.doubleowner.revibe.domain.item.entity.Item;
import com.doubleowner.revibe.domain.item.repository.ItemRepository;
import com.doubleowner.revibe.domain.like.entity.Like;
import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import com.doubleowner.revibe.domain.like.dto.LikeResponseDto;
import com.doubleowner.revibe.domain.like.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String Like_COUNT_KEY_PREFIX = "item:like:";

    // 관심상품등록, 해제 (좋아요 기능)
    @Transactional
    public boolean doLike(User loginUser, Long itemId) {
        String likeKey = Like_COUNT_KEY_PREFIX + itemId;

        if (Boolean.FALSE.equals(redisTemplate.hasKey(likeKey))) {
            ensureLikeCached(loginUser);
        }

        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(likeKey, loginUser.getId().toString()))) {
            redisTemplate.opsForSet().remove(likeKey, loginUser.getId().toString());
            return false;
        }

        redisTemplate.opsForSet().add(likeKey, loginUser.getId().toString());
        return true;
    }

    // 관심상품 목록 조회
    public List<LikeResponseDto> findLikes(User loginUser) {
        List<LikeResponseDto> likeDtos = getLikeFromRedis(loginUser);

        if (!likeDtos.isEmpty()) {
            return likeDtos; // Redis에 데이터가 있다면 바로 반환
        }

        //  Redis에 데이터가 없으면 MySQL에서 조회 후 Redis에 저장
        List<Like> likes = likeRepository.findByUser(loginUser);
        for (Like like : likes) {
            String key = Like_COUNT_KEY_PREFIX + like.getItem().getId();
            redisTemplate.opsForSet().add(key, loginUser.getId().toString());
        }

        return likes.stream().map(LikeResponseDto::toDto).toList();
    }

    // Redis에서 좋아요목록 가져오기
    private List<LikeResponseDto> getLikeFromRedis(User loginUser) {
        Set<String> itemKeys = redisTemplate.keys(Like_COUNT_KEY_PREFIX + "*");

        if (itemKeys == null || itemKeys.isEmpty()) {
            return List.of(); // Redis에 저장된 키가 없으면 빈 리스트 반환
        }

        List<LikeResponseDto> likes = itemKeys.stream()
                .filter(key -> Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, loginUser.getId().toString())))
                .map(key -> {
                    Long itemId = Long.parseLong(key.replace(Like_COUNT_KEY_PREFIX, ""));
                    return new LikeResponseDto(itemId);
                })
                .toList();

        return likes;
    }

    // 스케줄러로 redis에있는 데이터 mysql에 저장
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void syncLikeToMySQL() {
        Set<String> itemKeys = redisTemplate.keys(Like_COUNT_KEY_PREFIX + "*");

        if (itemKeys != null && !itemKeys.isEmpty()) {
            for (String likeKey : itemKeys) {
                Set<String> userIds = redisTemplate.opsForSet().members(likeKey);

                if (userIds != null && !userIds.isEmpty()) {
                    Long itemId = Long.parseLong(likeKey.replace(Like_COUNT_KEY_PREFIX, ""));
                    Item item = itemRepository.findByIdOrElseThrow(itemId);
                    item.setLikesCount((long) userIds.size());
                    itemRepository.save(item);

                    List<Like> newLikes = userIds.stream()
                            .map(userIdStr -> {
                                Long userId = Long.parseLong(userIdStr);
                                return likeRepository.existsByUserIdAndItemId(userId, itemId)
                                        ? null
                                        : new Like(userRepository.findByIdOrElseThrow(userId), item);
                            })
                            .filter(like -> like != null)
                            .toList();

                    if (!newLikes.isEmpty()) {
                        likeRepository.saveAll(newLikes);
                    }
                }
            }
        }
        deleteLikeFromMySQL();
    }

    // Redis에 없는 좋아요를 MySQL에서 삭제하는 메서드
    @Transactional
    public void deleteLikeFromMySQL() {
        Set<String> itemKeys = redisTemplate.keys(Like_COUNT_KEY_PREFIX + "*");
        List<Long> existingItemIds = itemKeys.stream()
                .map(key -> Long.parseLong(key.replace(Like_COUNT_KEY_PREFIX, "")))
                .toList();

        List<Like> likesToDelete = likeRepository.findAllNotInItemIds(existingItemIds);
        likeRepository.deleteAll(likesToDelete);
    }

    // 현재 사용자의 좋아요 목록(Mysql) 레디스에 저장
    private void ensureLikeCached(User loginUser) {
        List<Like> likes = likeRepository.findByUser(loginUser);

        for (Like like : likes) {
            String key = Like_COUNT_KEY_PREFIX + like.getItem().getId();
            if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
                redisTemplate.opsForSet().add(key, loginUser.getId().toString());
            }
        }
    }
}
