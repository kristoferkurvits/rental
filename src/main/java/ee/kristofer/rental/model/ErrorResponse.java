package ee.kristofer.rental.model;

import ee.kristofer.rental.constants.RestErrorType;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
public class ErrorResponse {
    private String requestId;
    private RestErrorType restErrorType;
    private List<String> errorCodes;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public RestErrorType getRestErrorType() {
        return restErrorType;
    }

    public void setRestErrorType(RestErrorType restErrorType) {
        this.restErrorType = restErrorType;
    }

    public List<String> getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(List<String> errorCodes) {
        this.errorCodes = errorCodes;
    }
}
