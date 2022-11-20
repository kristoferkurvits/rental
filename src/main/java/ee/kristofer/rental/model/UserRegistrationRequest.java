package ee.kristofer.rental.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Accessors(chain = true)
@Data
public class UserRegistrationRequest {

    @NotEmpty
    @Email
    @ApiModelProperty(required = true)
    private String email;
    @NotEmpty
    @Size(min = 8, max = 64)
    @ApiModelProperty(required = true)
    private String password;
    @NotEmpty
    @ApiModelProperty(required = true)
    private String name;
}
