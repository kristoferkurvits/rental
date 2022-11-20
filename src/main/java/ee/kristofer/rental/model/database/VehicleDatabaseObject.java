package ee.kristofer.rental.model.database;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import ee.kristofer.rental.constants.EntityType;
import ee.kristofer.rental.model.Coordinates;
import ee.kristofer.rental.util.StringUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Accessors(chain = true)
public class VehicleDatabaseObject {

    public VehicleDatabaseObject() {
        this.type = EntityType.VEHICLE;
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

    private String userId;

    @Max(100)
    @Min(0)
    private int stateOfCharge;

    @NotEmpty
    private Coordinates coordinates;

    private boolean inUse;

    @NotNull
    private Instant createdAt;
    @NotNull
    private Instant modifiedAt;

}
