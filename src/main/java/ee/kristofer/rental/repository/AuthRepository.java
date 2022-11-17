package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.Auth;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@RedisHash
public interface AuthRepository extends CrudRepository<Auth, String> {
}
