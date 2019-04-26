package net.lisanza.dropunit.impl.rest.controlers;

import net.lisanza.dropunit.impl.rest.constants.RequestMappings;
import net.lisanza.dropunit.impl.rest.services.AbstractDropUnitRequest;
import net.lisanza.dropunit.impl.rest.services.DropUnitCount;
import net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint;
import net.lisanza.dropunit.impl.rest.services.DropUnitService;
import net.lisanza.dropunit.impl.rest.services.data.ReceivedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Enumeration;
import java.util.Map;

@Path(RequestMappings.ROOT_SERVICE)
public class DropUnitController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DropUnitController.class);

    private final DropUnitCount dropUnitCount;
    private final DropUnitService dropUnitService;

    public DropUnitController(DropUnitService dropUnitService,
                              DropUnitCount dropUnitCount) {
        this.dropUnitService = dropUnitService;
        this.dropUnitCount = dropUnitCount;
    }

    @GET
    @Path("{any: .*}")
    public Response dropUnitGet(@Context HttpServletRequest request) {
        dropUnitCount.incrHttpGet();
        return dropUnit(request, "GET", "");
    }

    @POST
    @Path("{any: .*}")
    public Response dropUnitPost(@Context HttpServletRequest request,
                                 String content) {
        dropUnitCount.incrHttpPost();
        return dropUnit(request, "POST", content);
    }

    @PUT
    @Path("{any: .*}")
    public Response dropUnitPut(@Context HttpServletRequest request,
                                String content) {
        dropUnitCount.incrHttpPut();
        return dropUnit(request, "PUT", content);
    }

    @DELETE
    @Path("{any: .*}")
    public Response dropUnitDelete(@Context HttpServletRequest request,
                                   String content) {
        dropUnitCount.incrHttpDelete();
        return dropUnit(request, "DELETE", content);
    }

    public Response dropUnit(HttpServletRequest request, String method, String content) {
        ReceivedRequest receivedRequest = new ReceivedRequest()
                .withMethod(method)
                .withReceived(content);
        Enumeration<String> headerNames = request.getHeaderNames();
        String name;
        while (headerNames.hasMoreElements()) {
            name = headerNames.nextElement();
            receivedRequest.addHeader(name, request.getHeader(name));
        }
        return  dropUnit(request.getPathInfo(), request.getQueryString(), receivedRequest);
    }

    public Response dropUnit(String pathInfo, String queryString, ReceivedRequest receivedRequest) {
        if (receivedRequest == null) {
            LOGGER.warn("'received request' is missing!");
            throw new BadRequestException("'received request' is missing!");
        }
        // request validation
        StringBuffer url = new StringBuffer(pathInfo);
        if ((queryString != null) && !queryString.isEmpty()) {
            url.append('?').append(queryString);
        }
        DropUnitEndpoint endpoint = dropUnitService.lookupEndpoint(createDropUnitEndpoint(url.toString(), receivedRequest.getMethod()));
        if (endpoint == null) {
            String msg = String.format("'drop unit '%s' registration is missing!", url);
            LOGGER.warn(msg);
            dropUnitService.registerNotFound(url.toString(), receivedRequest);
            throw new NotFoundException(msg);
        }
        // request received
        endpoint.addReceived(receivedRequest);
        // validate
        validateRequestHeaders(endpoint, receivedRequest.getHeaders());
        if (endpoint.getRequest() != null) {
            validateRequestContentType(endpoint.getRequest().getContentType(), receivedRequest.getContentType());
            validateRequestContent(endpoint.getRequest(), receivedRequest.getBody());
        }
        // Response build up
        waitToRespond(endpoint.getDelay());
        Response.ResponseBuilder responseBuilder = buildResponse(endpoint);
        addContentType(endpoint, responseBuilder);
        addContent(endpoint, responseBuilder);
        return responseBuilder.build();
    }

    private Response.ResponseBuilder buildResponse(DropUnitEndpoint dropUnitEndpoint) {
        LOGGER.info("response code: {}", dropUnitEndpoint.getResponse().getCode());
        return Response.status(Response.Status.fromStatusCode(dropUnitEndpoint.getResponse().getCode()));
    }

    private void addContentType(DropUnitEndpoint dropUnitEndpoint, Response.ResponseBuilder responseBuilder) {
        if (dropUnitEndpoint.getResponse().getContentType() == null) {
            responseBuilder.header("Content-type", dropUnitEndpoint.getResponse().getContentType());
        }
    }

    private void addContent(DropUnitEndpoint dropUnitEndpoint, Response.ResponseBuilder responseBuilder) {
        if (dropUnitEndpoint.getResponse().getBody() != null) {
            responseBuilder.entity(dropUnitEndpoint.getResponse().getBody());
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
        throw new NotSupportedException("validate header " + name + " : endpoint (" + endpointValue + ") and request (" + requestValue + ") are NOT equal");
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
        throw new NotSupportedException("validate content-type: endpoint (" + endpointContentType + ") and request (" + requestContentType + ") are NOT equal");
    }

    private void validateRequestContent(AbstractDropUnitRequest dropUnitRequest, String content) {
        if (dropUnitRequest != null) {
            if (dropUnitRequest.doesRequestMatch(content)) {
                return;
            }
        }
        throw new NotSupportedException("validate: content and expected request-body are NOT matching");
    }

    private boolean isNullOrEmpty(String string) {
        return (string == null) || string.isEmpty();
    }

    private void waitToRespond(int delay) {
        if (0 < delay) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }

    public DropUnitEndpoint createDropUnitEndpoint(String uri, String method) {
        DropUnitEndpoint dropUnitEndpoint = new DropUnitEndpoint();
        dropUnitEndpoint.setUrl(uri);
        dropUnitEndpoint.setMethod(method);
        return dropUnitEndpoint;
    }
}
