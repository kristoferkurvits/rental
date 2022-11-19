package ee.kristofer.rental.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Accessors(chain = true)
public class Reservation {

    public Reservation() {
        this.start = Instant.now();
    }

    @NotEmpty
    private String id;

    private Instant start;
    @NotEmpty
    private Instant end;
    @NotNull
    private Coordinates startCoordinates;

    private Coordinates endCoordinates;

    private BigDecimal cost;
}
