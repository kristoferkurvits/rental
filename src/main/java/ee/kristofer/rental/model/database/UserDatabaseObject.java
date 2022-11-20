package ee.kristofer.rental.model.database;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ee.kristofer.rental.constants.EntityType;
import ee.kristofer.rental.model.Reservation;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Accessors(chain = true)
public class UserDatabaseObject {

    public UserDatabaseObject() {
        this.type = EntityType.USER;
        var time = Instant.now();
        this.createdAt = time;
        this.modifiedAt = time;
    }

    @Id
    @NotEmpty
    private String id;

    @NotEmpty
    @Indexed
    private EntityType type;

    @Indexed
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;

    private ReservationDatabaseObject ongoingReservation;

    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant modifiedAt;


}
