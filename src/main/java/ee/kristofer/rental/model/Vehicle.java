package ee.kristofer.rental.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import javax.validation.constraints.NotEmpty;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Vehicle {
    @NotEmpty
    private String id;
    @NotEmpty
    private String stateOfCharge;
    @NotEmpty
    private Coordinates coordinates;
}
