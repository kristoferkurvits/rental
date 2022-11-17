package ee.kristofer.rental.model;

import javax.validation.constraints.NotEmpty;

public class Coordinates {
    @NotEmpty
    private int x;
    @NotEmpty
    private int y;
}
