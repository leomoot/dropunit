package net.lisanza.dropunit.server.rest.controlers;

import net.lisanza.dropunit.server.services.DropUnitEndpoint;
import net.lisanza.dropunit.server.services.DropUnitEndpointRequest;
import net.lisanza.dropunit.server.services.data.ReceivedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ValidationException;
import java.util.Map;

public class EndpointValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointValidator.class);

    protected void validate(DropUnitEndpoint endpoint, ReceivedRequest receivedRequest) {
        // validate
        validateRequestHeaders(endpoint, receivedRequest.getHeaders());
        if (endpoint.getRequest() != null) {
            validateRequestContentType(endpoint.getRequest().getContentType(), receivedRequest.getContentType());
            validateRequestContent(endpoint.getRequest(), receivedRequest.getBody());
        }
    }

    protected void validateRequestHeaders(DropUnitEndpoint dropUnitEndpoint, Map<String, String> requestHeaders) {
        for (String name : dropUnitEndpoint.getHeaders().keySet()) {
            validateRequestHeader(name, dropUnitEndpoint.getHeaders().get(name), requestHeaders.get(name));
        }
    }

    protected void validateRequestHeader(String name, String endpointValue, String requestValue) {
        if (isNullOrEmpty(endpointValue)
                && isNullOrEmpty(requestValue)) {
            return;
        }
        if (!isNullOrEmpty(endpointValue)
                && !isNullOrEmpty(requestValue)
                && (endpointValue.equals(requestValue))) {
            return;
        }
        LOGGER.error("validate header {}: endpoint ({}) and request ({}) are NOT equal", name, endpointValue, requestValue);
        throw new ValidationException("validate header " + name + " : endpoint (" + endpointValue + ") and request (" + requestValue + ") are NOT equal");
    }

    protected void validateRequestContentType(String endpointContentType, String requestContentType) {
        if (isNullOrEmpty(endpointContentType)
                && isNullOrEmpty(requestContentType)) {
            return;
        }
        if (!isNullOrEmpty(endpointContentType)
                && !isNullOrEmpty(requestContentType)
                && (endpointContentType.equals(requestContentType))) {
            return;
        }
        LOGGER.error("validate content-type: endpoint ({}) and request ({}) are NOT equal", endpointContentType, requestContentType);
        throw new ValidationException("validate content-type: endpoint (" + endpointContentType + ") and request (" + requestContentType + ") are NOT equal");
    }

    private void validateRequestContent(DropUnitEndpointRequest dropUnitRequest, String content) {
        if (dropUnitRequest != null) {
            if (dropUnitRequest.doesRequestMatch(content)) {
                return;
            }
        }
        throw new ValidationException("validate: content and expected request-body are NOT matching");
    }

    private boolean isNullOrEmpty(String string) {
        return (string == null) || string.isEmpty();
    }

}
