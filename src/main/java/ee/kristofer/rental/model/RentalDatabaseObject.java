package ee.kristofer.rental.model;


import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class RentalDatabaseObject {

    @Id
    @NotEmpty
    private String id;
    @Indexed
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;

    private Vehicle vehicle;
    /*
        This isn't a nice solution because eventually the reservations stack up and reading them all into memory
        will cause problems in the future. Maybe pagination?
    */
    private List<Reservation>reservations;

}
