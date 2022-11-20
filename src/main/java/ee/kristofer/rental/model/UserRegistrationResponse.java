package ee.kristofer.rental.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRegistrationResponse {
    private String userId;
}
