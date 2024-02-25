package com.example.myredistest.jedis.user.domain;

import com.example.myredistest.cache.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJedisRepository extends JpaRepository<User,Long> {
}
