package ee.kristofer.rental.exception;

public final class RentalException extends RuntimeException {
    public RentalException(Throwable cause) { super(cause); }

    public RentalException(String message) { super(message); }

    public RentalException(String message, Throwable cause) { super(message, cause); }
}
