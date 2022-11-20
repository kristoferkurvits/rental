package ee.kristofer.rental.repository;

import ee.kristofer.rental.model.database.ReservationDatabaseObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends MongoRepository<ReservationDatabaseObject, String> {
    Page<ReservationDatabaseObject> findByUserId(String userId, Pageable pageable);
}
