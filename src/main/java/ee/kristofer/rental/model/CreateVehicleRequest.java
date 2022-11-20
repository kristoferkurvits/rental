package ee.kristofer.rental.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@Accessors(chain = true)
public class CreateVehicleRequest {

    @Max(100)
    @Min(0)
    @ApiModelProperty(required = true)
    private int stateOfCharge;
    @ApiModelProperty(required = true)
    private Coordinates coordinates;
}
