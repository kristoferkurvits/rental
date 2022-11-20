package ee.kristofer.rental.api;

import ee.kristofer.rental.RentalApplication;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@SpringBootTest(
        classes = RentalApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@SpringBootConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
public class IntegrationTestBase {
}
