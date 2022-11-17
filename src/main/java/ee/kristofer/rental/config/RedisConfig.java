package ee.kristofer.rental.config;

import ee.kristofer.rental.model.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@PropertySource("classpath:application-localdev.properties")
@RequiredArgsConstructor
//TODO Can't detect correct properties file, find a solution
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private String timeout;

    @Bean(name = "jedisConnectionFactory")
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(
            @Qualifier(value = "jedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer());
        template.setValueSerializer(stringRedisSerializer());
        return template;
    }

    @Bean(name = "redisUserConnectionFactory")
    public JedisConnectionFactory redisUserConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();

        setRedisProperties(redisConfiguration);

        JedisClientConfiguration jedisClientConfiguration =
                JedisClientConfiguration.builder()
                        .connectTimeout(Duration.ofMillis(Long.parseLong(timeout)))
                        .build();

        return new JedisConnectionFactory(redisConfiguration, jedisClientConfiguration);
    }

    @Bean(name = "userRedisTemplate")
    public RedisTemplate<String, Object> userRedisTemplate(
            @Qualifier(value = "redisUserConnectionFactory")
                    RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer());
        template.setValueSerializer(stringRedisSerializer());
        return template;
    }

    @Bean(name = "redisRegistrationTokenConnectionFactory")
    public JedisConnectionFactory redisRegistrationTokenConnectionFactory() {
        RedisStandaloneConfiguration redisConfiguration = new RedisStandaloneConfiguration();

        setRedisProperties(redisConfiguration);

        JedisClientConfiguration jedisClientConfiguration =
                JedisClientConfiguration.builder()
                        .connectTimeout(Duration.ofMillis(Long.parseLong(timeout)))
                        .build();

        return new JedisConnectionFactory(redisConfiguration, jedisClientConfiguration);
    }

    @Bean(name = "registrationTokenRedisTemplate")
    public RedisTemplate<String, Object> registrationTokenRedisTemplate(
            @Qualifier(value = "redisRegistrationTokenConnectionFactory")
                    RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer());
        template.setValueSerializer(stringRedisSerializer());
        return template;
    }

    @Bean(name = "stringRedisSerializer")
    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    private void setRedisProperties(RedisStandaloneConfiguration redisConfiguration) {
        redisConfiguration.setHostName(host);
        redisConfiguration.setPort(port);
    }

}
