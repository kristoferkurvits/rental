package ee.kristofer.rental.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RentalDb {
    private User user;
    private Vehicle vehicle;
    /*
        This isn't a nice solution because eventually the reservations stack up and reading them all into memory
        will cause problems in the future. Maybe pagination?
    */
    private List<Reservation>reservations;
}
