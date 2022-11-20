package ee.kristofer.rental.model.database;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ee.kristofer.rental.constants.EntityType;
import ee.kristofer.rental.model.Coordinates;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Accessors(chain = true)
@Document(collection = "reservationDatabaseObject")
public class ReservationDatabaseObject {

    public ReservationDatabaseObject() {
        var now = Instant.now();
        this.start = now;
        this.active = true;
        this.createdAt = now;
        this.modifiedAt = now;
        this.type = EntityType.RESERVATION;
    }

    @Id
    @NotEmpty
    private String id;

    @NotNull
    @Indexed
    private EntityType type;

    @NotEmpty
    @Indexed
    private String userId;

    @NotEmpty
    private String vehicleId;

    private Instant start;
    @NotEmpty
    private Instant end;
    @NotNull
    private Coordinates startCoordinates;

    private Coordinates endCoordinates;

    private BigDecimal cost;

    private boolean active;

    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant modifiedAt;
}