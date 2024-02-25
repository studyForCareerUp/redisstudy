package com.example.myredistest.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.List;

@EnableCaching
@Configuration
public class CacheConfig {

    public static final String CACHE1 = "cache1";
    public static final String CACHE2 = "cache2";


    @AllArgsConstructor
    @Builder
    @Getter
    public static class CacheProperty {
        private String name;
        private Integer ttl;
    }

    // 레디스 캐시를 사용하기 위해 설정  @Cacheable 과 관련, 따로 빈 설정 안해줘도 상관은 없음
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {

        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator
                .builder()
                .allowIfSubType(Object.class)
                .build();

        var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) //역직렬화 과정에서 모르는 값이 있으면 무효화 해줌
                .registerModule(new JavaTimeModule())
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL) //해당 클래스와 패키지 정보가 일부 포함되서 진행됨
                .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS); // 시간정보를 제대로 넣어주기 위해


        List<CacheProperty> properties = List.of(new CacheProperty(CACHE1, 300), new CacheProperty(CACHE2, 30));

        return (builder -> {
            properties.forEach(i -> {
                builder.withCacheConfiguration(i.getName(), RedisCacheConfiguration
                        .defaultCacheConfig()
                        .disableCachingNullValues()
                        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
                        .entryTtl(Duration.ofSeconds(i.getTtl())));

            });
        });

    }
}
