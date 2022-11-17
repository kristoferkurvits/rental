package ee.kristofer.rental.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.NotEmpty;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Reservation {
    @NotEmpty
    private String id;
    @NotEmpty
    private Instant start;
    @NotEmpty
    private Instant end;
    @NotEmpty
    private Coordinates startCoordinates;
    @NotEmpty
    private Coordinates endCoordinates;
    @NotEmpty
    private String cost;
}
