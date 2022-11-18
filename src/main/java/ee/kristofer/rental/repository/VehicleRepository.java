package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.database.VehicleDatabaseObject;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleRepository extends MongoRepository<VehicleDatabaseObject, String> {
}
