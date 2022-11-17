package ee.kristofer.rental.exception;

import ee.kristofer.rental.constants.RestErrorType;

public final class UnprocessableEntityException extends RuntimeException {
    private RestErrorType restErrorType;

    public UnprocessableEntityException(Throwable cause) { super(cause); }

    public UnprocessableEntityException(String message) { super(message); }

    public UnprocessableEntityException(String message, Throwable cause) { super(message, cause); }

    public UnprocessableEntityException(RestErrorType restErrorType, String message) {
        super(message);
        this.restErrorType = restErrorType;
    }

    public RestErrorType getRestErrorType() {
        return restErrorType;
    }
}
