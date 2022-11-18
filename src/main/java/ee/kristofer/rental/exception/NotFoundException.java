package ee.kristofer.rental.exception;

public final class NotFoundException extends RuntimeException {
    public NotFoundException(Throwable cause) { super(cause); }

    public NotFoundException(String message) { super(message); }

    public NotFoundException(String message, Throwable cause) { super(message, cause); }
}
