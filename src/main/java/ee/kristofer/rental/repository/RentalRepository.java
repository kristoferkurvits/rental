package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.RentalDb;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RentalRepository extends MongoRepository<RentalDb, String> {
}

