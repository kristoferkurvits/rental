package ee.kristofer.rental.handler;

import ee.kristofer.rental.constants.RestErrorType;
import ee.kristofer.rental.exception.AuthorizationException;
import ee.kristofer.rental.exception.UnprocessableEntityException;
import ee.kristofer.rental.model.ErrorResponse;
import ee.kristofer.rental.model.Response;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ee.kristofer.rental.constants.Constants.REQUEST_ID;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static String convertValidationMessage(FieldError fieldError) {
        return switch (fieldError.getCode() == null ? "" : fieldError.getCode()) {
            case "NotNull", "NotBlank", "NotEmpty" -> fieldError.getField() + "REQUIRED";
            case "Max" -> fieldError.getField() + "_INVALID_MAX";
            case "MIN" -> fieldError.getField() + "_INVALID_MIN";
            case "PATTERN", "Email" -> fieldError.getField() + "_INVALID_PATTERN";
            default -> fieldError.getField() + "INVALID";
        };
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    protected ResponseEntity<Object> handleUnprocessableEntityException(final UnprocessableEntityException ex) {
        return Response.nok(HttpStatus.UNPROCESSABLE_ENTITY, createUnprocessableEntityResponse(ex));
    }

    @ExceptionHandler(AuthorizationException.class)
    protected ResponseEntity<Object> handleAuthorizationException(final AuthorizationException ex) {
        return Response.nok(HttpStatus.UNAUTHORIZED, createAuthorizationResponse(ex));
    }

    private ErrorResponse createAuthorizationResponse(AuthorizationException ex) {
        return createErrorResponse(ex, RestErrorType.INVALID_API_KEY);
    }

    private ErrorResponse createUnprocessableEntityResponse(UnprocessableEntityException ex) {
        RestErrorType restErrorType = Objects.nonNull(ex.getRestErrorType()) ?
                ex.getRestErrorType() : RestErrorType.INVALID_REQUEST;
        return createErrorResponse(ex, restErrorType);
    }

    private ErrorResponse createErrorResponse(Exception ex, RestErrorType restErrorType) {
        var errorResponse = new ErrorResponse();
        errorResponse.setRequestId(ThreadContext.get(REQUEST_ID));
        errorResponse.setRestErrorType(restErrorType);
        errorResponse.setErrorCodes(getErrorCodes(ex));
        return errorResponse;
    }

    private List<String> getErrorCodes(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException mex) {
            return mex.getBindingResult().getFieldErrors().stream()
                    .map(RestExceptionHandler::convertValidationMessage)
                    .toList();
        }
        return Collections.singletonList(ex.getMessage());
    }


}
