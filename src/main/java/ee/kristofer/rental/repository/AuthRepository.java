package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.Auth;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;

public interface AuthRepository {
    void save(Auth auth);
    Auth findById(String id);
    void update(Auth auth);
    void delete(String id);
}
