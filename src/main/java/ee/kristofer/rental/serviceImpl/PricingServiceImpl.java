package ee.kristofer.rental.serviceImpl;

import ee.kristofer.rental.service.PricingService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

@Log4j2
@Service
public class PricingServiceImpl implements PricingService {

    private static final int STARTING_PRICE = 1;
    private static final BigDecimal TEN_MINUTES = BigDecimal.valueOf(10);
    private static final BigDecimal PRICE_PER_MINUTE_BEFORE_TEN_MINUTES = BigDecimal.valueOf(0.5);
    private static final BigDecimal PRICE_PER_MINUTE_AFTER_TEN_MINUTES = BigDecimal.valueOf(0.3);
    private static final BigDecimal ZERO = BigDecimal.valueOf(0);
    private static final BigDecimal ONE_MINUTE = BigDecimal.valueOf(60);

    @Override
    public BigDecimal calculatePrice(Instant start, Instant end) {
        BigDecimal duration = BigDecimal.valueOf(Duration.between(start, end).toSeconds());
        BigDecimal minutes = duration.divide(ONE_MINUTE, RoundingMode.DOWN);
        BigDecimal seconds = duration.remainder(ONE_MINUTE);

        BigDecimal result = BigDecimal.valueOf(STARTING_PRICE);
        if (minutes.compareTo(TEN_MINUTES) == BigDecimal.ONE.intValue()) {
            result = result.add(TEN_MINUTES.multiply(PRICE_PER_MINUTE_BEFORE_TEN_MINUTES));
            result = result.add(minutes.subtract(TEN_MINUTES).multiply(PRICE_PER_MINUTE_AFTER_TEN_MINUTES));
            if (seconds.compareTo(ZERO) == BigDecimal.ONE.intValue()) {
                result = result.add(PRICE_PER_MINUTE_AFTER_TEN_MINUTES);
            }
            return result;
        }
        result = result.add(minutes.multiply(PRICE_PER_MINUTE_BEFORE_TEN_MINUTES));
        if (seconds.compareTo(ZERO) == BigDecimal.ONE.intValue()) {
            result = result.add(PRICE_PER_MINUTE_BEFORE_TEN_MINUTES);
        }
        return result;
    }
}
