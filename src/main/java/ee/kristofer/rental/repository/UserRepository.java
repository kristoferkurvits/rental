package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.database.UserDatabaseObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends MongoRepository<UserDatabaseObject, String> {
    UserDatabaseObject findByEmail(String email);
}

