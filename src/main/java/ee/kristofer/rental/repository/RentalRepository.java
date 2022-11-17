package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.RentalDb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends MongoRepository<RentalDb, String> {
}

