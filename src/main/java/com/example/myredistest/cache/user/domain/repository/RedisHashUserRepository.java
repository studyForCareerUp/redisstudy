package com.example.myredistest.cache.user.domain.repository;


import com.example.myredistest.cache.user.domain.entity.RedisHashUser;
import org.springframework.data.repository.CrudRepository;

public interface RedisHashUserRepository  extends CrudRepository<RedisHashUser, Long> {
}
