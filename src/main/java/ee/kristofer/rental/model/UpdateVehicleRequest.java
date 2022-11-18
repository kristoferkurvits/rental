package ee.kristofer.rental.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class UpdateVehicleRequest {

    @NotEmpty
    private String id;

    @Max(100)
    @Min(0)
    private Integer stateOfCharge;

    private Coordinates coordinates;
}
