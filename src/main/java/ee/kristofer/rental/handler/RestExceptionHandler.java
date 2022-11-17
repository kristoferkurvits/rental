package ee.kristofer.rental.handler;

import ee.kristofer.rental.constants.RestErrorType;
import ee.kristofer.rental.exception.UnprocessableEntityException;
import ee.kristofer.rental.model.ErrorResponse;
import ee.kristofer.rental.model.Response;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler
    protected ResponseEntity<Object> handleUnprocessableEntityException(final UnprocessableEntityException ex) {
        return Response.nok(HttpStatus.UNPROCESSABLE_ENTITY, createUnprocessableEntityResponse(ex));
    }

    private ErrorResponse createUnprocessableEntityResponse(UnprocessableEntityException ex) {
        RestErrorType restErrorType = Objects.nonNull(ex.getRestErrorType()) ?
                ex.getRestErrorType() : RestErrorType.INVALID_REQUEST;
        var errorResponse = new ErrorResponse();
        errorResponse.setRequestId(ThreadContext.get(REQUEST_ID));
        errorResponse.setRestErrorType(restErrorType);
        errorResponse.setErrorCodes(getErrorCodes(ex));

        return errorResponse;

    }

    private List<String> getErrorCodes(UnprocessableEntityException ex) {
        return Collections.singletonList(ex.getMessage());
    }


}
