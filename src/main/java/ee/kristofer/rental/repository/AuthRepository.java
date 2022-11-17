package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.Auth;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends CrudRepository<Auth, String> {
}
