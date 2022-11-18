package ee.kristofer.rental.util;

public class StringUtil {
    private StringUtil() {
    }

    public static boolean isNullOrEmpty(String string) {
        return (string == null || string.length() == 0);
    }
}
