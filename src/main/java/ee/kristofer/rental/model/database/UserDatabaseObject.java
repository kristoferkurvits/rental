package ee.kristofer.rental.model.database;


import ee.kristofer.rental.constants.EntityType;
import ee.kristofer.rental.model.Reservation;
import ee.kristofer.rental.model.Vehicle;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Accessors(chain = true)
public class UserDatabaseObject {

    public UserDatabaseObject() {
        this.type = EntityType.USER;
    }

    @Id
    @NotEmpty
    private String id;

    @NotEmpty
    @Indexed
    private EntityType type;

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
