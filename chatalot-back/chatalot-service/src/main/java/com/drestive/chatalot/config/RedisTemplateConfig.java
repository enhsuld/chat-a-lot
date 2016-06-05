package com.drestive.chatalot.config;

import com.drestive.chatalot.service.identity.model.CachedAuthenticationDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by mustafa on 03/06/2016.
 */

@Configuration
public class RedisTemplateConfig {

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    RedisTemplate<String, CachedAuthenticationDetails> redisTemplate() {
        final RedisTemplate<String, CachedAuthenticationDetails> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(CachedAuthenticationDetails.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(CachedAuthenticationDetails.class));
        return template;
    }

    @Bean
    RedisTemplate<String, String> redisKeyTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
