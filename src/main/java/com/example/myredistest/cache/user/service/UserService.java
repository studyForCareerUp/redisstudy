package com.example.myredistest.cache.user.service;


import com.example.myredistest.cache.user.domain.entity.RedisHashUser;
import com.example.myredistest.cache.user.domain.entity.User;
import com.example.myredistest.cache.user.domain.repository.RedisHashUserRepository;
import com.example.myredistest.cache.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.example.myredistest.config.CacheConfig.CACHE1;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, Object> objectRedisTemplate;
    private final RedisHashUserRepository redisHashUserRepository;


    public User getUser(final Long id) {

        var key = "users:%d".formatted(id);
        // 1. cache get
//        var cacheUser = userRedisTemplate.opsForValue().get(key);
        var cacheUser = objectRedisTemplate.opsForValue().get(key);
        if (cacheUser != null) {
            return (User) cacheUser;
        }

        User user = userRepository.findById(id).orElseThrow();
//        userRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(30) /* 30초뒤에는 데이터 날아가게 TTL 설장 */ );
        objectRedisTemplate.opsForValue().set(key, user, Duration.ofSeconds(30) /* 30초뒤에는 데이터 날아가게 TTL 설장 */);

        return user;
    }

    public RedisHashUser getUser2(final Long id) {

        return redisHashUserRepository.findById(id).orElseGet(() -> {

            User user = userRepository.findById(id).orElseThrow();

            return redisHashUserRepository.save(RedisHashUser.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build());
        });

    }

    @Cacheable(cacheNames = CACHE1, key = "'user:' + #id")
    public User getUser3(final Long id) {

        return userRepository.findById(id).orElseThrow();

    }

}
