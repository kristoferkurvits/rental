package ee.kristofer.rental.config;

import ee.kristofer.rental.model.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    @Value("#{spring.redis.port}")
    private int redisPort;

    @Value("#{spring.redis.host}")
    private String redisHost;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConFactory
                = new JedisConnectionFactory();
        //TODO set methods deprecated
        jedisConFactory.setHostName(redisHost);
        jedisConFactory.setPort(redisPort);
        return jedisConFactory;
    }

    @Bean
    public RedisTemplate<String, Auth> redisTemplate() {
        RedisTemplate<String, Auth> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }


}
