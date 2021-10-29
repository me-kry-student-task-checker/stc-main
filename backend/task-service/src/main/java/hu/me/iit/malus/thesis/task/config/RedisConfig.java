package hu.me.iit.malus.thesis.task.config;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class RedisConfig {

    @Bean
    public RedisTemplate<String, List<Long>> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, List<Long>> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}