package ee.kristofer.rental.exception;

public final class AuthorizationException extends RuntimeException {
    public AuthorizationException() {}

    public AuthorizationException(Throwable cause) { super(cause); }

    public AuthorizationException(String message) { super(message); }

    public AuthorizationException(String message, Throwable cause) { super(message, cause); }
}
