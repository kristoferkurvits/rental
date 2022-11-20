package ee.kristofer.rental.util;

import org.apache.logging.log4j.ThreadContext;

import static ee.kristofer.rental.constants.Constants.USER_ID_PARAM;

public class ValidationUtil {
    private ValidationUtil() {}

    public static boolean validUser(String userId) {
        return userId.equals(ThreadContext.get(USER_ID_PARAM));
    }
}
