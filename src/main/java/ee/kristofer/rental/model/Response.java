package ee.kristofer.rental.model;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class Response {
    private Response() {}

    public static <T> ResponseEntity<T> ok(T body) {
        return createResponse(HttpStatus.OK, body);
    }

    public static <T> ResponseEntity<T> ok() {
        return createResponse(HttpStatus.OK, null);
    }

    public static <T> ResponseEntity<T> ok(HttpStatus statusCode) {
        return createResponse(statusCode, null);
    }

    public static <T> ResponseEntity<T> nok(HttpStatus statusCode) {
        return createResponse(statusCode, null);
    }

    public static <T> ResponseEntity<T> nok(HttpStatus statusCode, T body) {
        return createResponse(statusCode, null);
    }

    private static <T> ResponseEntity<T> createResponse(HttpStatus statusCode, T body) {
        return new ResponseEntity<>(body, createHeaders(), statusCode);
    }

    private static HttpHeaders createHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
