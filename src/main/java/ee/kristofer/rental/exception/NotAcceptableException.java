package ee.kristofer.rental.exception;

import ee.kristofer.rental.constants.RestErrorType;

public class NotAcceptableException extends RuntimeException {
    private RestErrorType restErrorType;

    public NotAcceptableException(Throwable cause) { super(cause); }

    public NotAcceptableException(String message) { super(message); }

    public NotAcceptableException(String message, Throwable cause) { super(message, cause); }

    public RestErrorType getRestErrorType() {
        return restErrorType;
    }
}
