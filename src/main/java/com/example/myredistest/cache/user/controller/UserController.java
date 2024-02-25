package com.example.myredistest.cache.user.controller;

import com.example.myredistest.cache.user.domain.entity.RedisHashUser;
import com.example.myredistest.cache.user.domain.entity.User;
import com.example.myredistest.cache.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @GetMapping("redishash-users/{id}")
    public RedisHashUser getUser2(@PathVariable Long id) {
        return userService.getUser2(id);
    }

    @GetMapping("cache-users/{id}")
    public User getUser3(@PathVariable Long id) {
        return userService.getUser3(id);
    }
}
