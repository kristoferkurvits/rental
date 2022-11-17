package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.RentalDatabaseObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends MongoRepository<RentalDatabaseObject, String> {
    RentalDatabaseObject findByEmail(String email);
}

