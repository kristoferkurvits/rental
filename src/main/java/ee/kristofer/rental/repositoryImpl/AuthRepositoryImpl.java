package ee.kristofer.rental.repositoryImpl;

import ee.kristofer.rental.model.Auth;
import ee.kristofer.rental.repository.AuthRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AuthRepositoryImpl implements AuthRepository {
    private static final String AUTH = "AUTH";

    private RedisTemplate redisTemplate;
    private HashOperations hashOperations;

    public AuthRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }
    @Override
    public void save(Auth auth) {
        hashOperations.put(AUTH,auth.getUserId(),auth);
    }

    @Override
    public Auth findById(String id) {
        return (Auth)hashOperations.get(AUTH,id);
    }
    @Override
    public void update(Auth auth) {
        save(auth);
    }
    @Override
    public void delete(String id) {
        hashOperations.delete(AUTH,id);
    }
}
