package ee.kristofer.rental.service;

import ee.kristofer.rental.serviceImpl.PricingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("localdev")
@SpringBootTest(classes = PricingServiceImpl.class)
public class PricingServiceTests {

    @Autowired
    PricingServiceImpl pricingService;

    @BeforeEach
    void beforeAll() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculatePriceOverTenMinutes() {
        var start = Instant.parse("2022-12-03T10:00:00.00Z");
        var stop = Instant.parse("2022-12-03T10:12:11.00Z");
        assertEquals(BigDecimal.valueOf(6.9), pricingService.calculatePrice(start, stop));
    }

    @Test
    void calculatePriceUnderTenMinutes() {
        var start = Instant.parse("2022-12-03T10:00:00.00Z");
        var stop = Instant.parse("2022-12-03T10:09:11.00Z");
        assertEquals(BigDecimal.valueOf(6.0), pricingService.calculatePrice(start, stop));
    }

    @Test
    void calculatePriceUnderMinute() {
        var start = Instant.parse("2022-12-03T10:00:00.00Z");
        var stop = Instant.parse("2022-12-03T10:00:45.00Z");
        assertEquals(BigDecimal.valueOf(1.5), pricingService.calculatePrice(start, stop));
    }

    @Test
    void calculatePriceTenMinutes() {
        var start = Instant.parse("2022-12-03T10:00:00.00Z");
        var stop = Instant.parse("2022-12-03T10:10:00.00Z");
        assertEquals(BigDecimal.valueOf(6.0), pricingService.calculatePrice(start, stop));
    }
}
