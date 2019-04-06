package net.lisanza.dropunit.impl.rest.controlers;

import net.lisanza.dropunit.impl.rest.constants.RequestMappings;
import net.lisanza.dropunit.impl.rest.services.AbstractDropUnitRequest;
import net.lisanza.dropunit.impl.rest.services.DropUnitCount;
import net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint;
import net.lisanza.dropunit.impl.rest.services.DropUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_XML)
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
        // request validation
        DropUnitEndpoint endpoint;
        if ((request.getQueryString() == null) || request.getQueryString().isEmpty()) {
            endpoint = lookupEndpoint(createDropUnitEndpoint(request.getPathInfo(), method));
        } else {
            endpoint = lookupEndpoint(createDropUnitEndpoint(request.getPathInfo() + "?" + request.getQueryString(), method));
        }
        endpoint.incr();
        if (endpoint.getRequest() != null) {
            validateRequestContentType(endpoint.getRequest(), request);
            validateRequestContent(endpoint.getRequest(), content);
        }
        // request received
        endpoint.addReceived(content);
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

    protected void validateRequestContentType(AbstractDropUnitRequest dropUnitRequest, HttpServletRequest httpRequest) {
        if (dropUnitRequest != null) {
            validateRequestContentType(dropUnitRequest.getContentType(), httpRequest.getHeader("Content-type"));
        }
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
        LOGGER.error("endpoint ({}) and ({}) content content-type are NOT equal", endpointContentType, requestContentType);
        throw new NotSupportedException("validate content-type: endpoint (" + endpointContentType + ") and content (" + requestContentType + ") are NOT equal");
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

    private DropUnitEndpoint lookupEndpoint
            (DropUnitEndpoint dropUnitEndpoint) {
        DropUnitEndpoint result = dropUnitService.lookupEndpoint(dropUnitEndpoint);
        if (result == null) {
            String msg = String.format("'drop unit '%s' registration is missing!", dropUnitEndpoint.getUrl());
            LOGGER.warn(msg);
            throw new NotFoundException(msg);
        }

        LOGGER.info("lookupEndpoint -> {}", result);
        return result;
    }

    public DropUnitEndpoint createDropUnitEndpoint(String uri, String
            method) {
        DropUnitEndpoint dropUnitEndpoint = new DropUnitEndpoint();
        dropUnitEndpoint.setUrl(uri);
        dropUnitEndpoint.setMethod(method);
        return dropUnitEndpoint;
    }
}
