package ee.kristofer.rental.config;

import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import ee.kristofer.rental.model.Auth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@PropertySource("classpath:application-localdev.properties")
public class RedisConfig {

    @Bean
    public RedisTemplate<Long, Auth> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Long, Auth> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
