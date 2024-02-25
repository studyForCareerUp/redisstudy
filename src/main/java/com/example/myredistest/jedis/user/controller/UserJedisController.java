package com.example.myredistest.jedis.user.controller;


import com.example.myredistest.cache.user.domain.entity.User;
import com.example.myredistest.jedis.user.domain.UserJedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@RestController
@RequiredArgsConstructor
public class UserJedisController {

    private final UserJedisRepository userRepository;
    private final JedisPool jedisPool;

    @GetMapping("/users/{id}/email")
    public String getUserEmail(@PathVariable Long id) {
        try (Jedis jedis = jedisPool.getResource()) {
            String userEmailRedisKey = "users:%d:email";
            //1. request th cache
            String userEmail = jedis.get(userEmailRedisKey.formatted(id));

            // 2. check value is empty
            if (StringUtils.hasText(userEmail)) {
                return userEmail;
            }

            // 3. else db
            userEmail = userRepository.findById(id).orElse(User.builder().build()).getEmail();

            // 4. cache
            jedis.setex(userEmailRedisKey.formatted(id), 600, userEmail); //TTL 주기, 일정 시간 뒤에 키가 사라짐 (초단위)
            return userEmail;

        }
    }

}
