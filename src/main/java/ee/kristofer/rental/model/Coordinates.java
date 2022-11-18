package ee.kristofer.rental.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Accessors(chain = true)
public class Coordinates {

    @Min(-180)
    @Max(180)
    @Nullable
    private Double longitude;

    @Min(-90)
    @Max(90)
    @Nullable
    private Double latitude;
}
