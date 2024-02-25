package com.example.myredistest;

import com.example.myredistest.cache.user.domain.entity.User;
import com.example.myredistest.jedis.user.domain.UserJedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jmx.support.RegistrationPolicy;

@SpringBootApplication
@RequiredArgsConstructor
@EnableJpaAuditing // created_at, updated_at 자동으로 넣어줌
@EnableMBeanExport(registration= RegistrationPolicy.IGNORE_EXISTING) //jmx 빈 충돌 때문에 작성,
public class RedisApplication implements ApplicationRunner {

    private final UserJedisRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userRepository.save(User.builder().name("greg").email("greg@naver.com").build());
        userRepository.save(User.builder().name("bob").email("bob@naver.com").build());
        userRepository.save(User.builder().name("tina").email("tina@naver.com").build());
        userRepository.save(User.builder().name("lion").email("lion@naver.com").build());

    }

}
