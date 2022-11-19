package ee.kristofer.rental.constants;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestConstants {
    public static final String ONGOING_RESERVATION_ID = "451fc576-2f46-4eeb-ba3b-8a229c379b7c";
    public static final String VEHICLE_ID = "4cd44bf0-48e9-4a4b-b93b-14b62a7688f3";
    public static final String USER_ID_WITHOUT_VEHICLE = "96fc87ad-65f4-4469-93e0-e627078f2482";
    public static final String USER_ID_WITH_VEHICLE = "6718c46b-0f66-4ea5-a412-d3facbedeb3f";
    public static final String EMAIL = "qwe@hot.ee";
    public static final String PASSWORD = "encryptedpassword";
    public static final String NAME = "name";
    public static final String APIKEY_FOR_USER_WITH_VEHICLE = new String(Base64.getEncoder().encode(USER_ID_WITH_VEHICLE.getBytes(StandardCharsets.UTF_8)));
    public static final String APIKEY_FOR_USER_WITHOUT_VEHICLE = new String(Base64.getEncoder().encode(USER_ID_WITHOUT_VEHICLE.getBytes(StandardCharsets.UTF_8)));
}
