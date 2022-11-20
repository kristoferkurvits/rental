package ee.kristofer.rental.service;

import java.math.BigDecimal;
import java.time.Instant;

public interface PricingService {
    BigDecimal calculatePrice(Instant start, Instant end);
}
