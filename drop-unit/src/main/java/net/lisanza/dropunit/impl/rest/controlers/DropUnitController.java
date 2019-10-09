package net.lisanza.dropunit.impl.rest.controlers;

import net.lisanza.dropunit.impl.rest.constants.RequestMappings;
import net.lisanza.dropunit.impl.rest.services.DropUnitCount;
import net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint;
import net.lisanza.dropunit.impl.rest.services.DropUnitService;
import net.lisanza.dropunit.impl.rest.services.data.ReceivedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.Enumeration;

@Path(RequestMappings.ROOT_SERVICE)
public class DropUnitController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DropUnitController.class);

    private final DropUnitCount dropUnitCount;
    private final DropUnitService dropUnitService;
    private final EndpointValidator validator = new EndpointValidator();

    public DropUnitController(DropUnitService dropUnitService,
                              DropUnitCount dropUnitCount) {
        this.dropUnitService = dropUnitService;
        this.dropUnitCount = dropUnitCount;
    }

    @GET
    @Path("{any: .*}")
    public Response dropUnitGet(@Context HttpServletRequest request) {
        dropUnitCount.incrHttpGet();
        return executeDropUnit(request, "GET", "");
    }

    @POST
    @Path("{any: .*}")
    public Response dropUnitPost(@Context HttpServletRequest request,
                                 String content) {
        dropUnitCount.incrHttpPost();
        return executeDropUnit(request, "POST", content);
    }

    @PUT
    @Path("{any: .*}")
    public Response dropUnitPut(@Context HttpServletRequest request,
                                String content) {
        dropUnitCount.incrHttpPut();
        return executeDropUnit(request, "PUT", content);
    }

    @DELETE
    @Path("{any: .*}")
    public Response dropUnitDelete(@Context HttpServletRequest request,
                                   String content) {
        dropUnitCount.incrHttpDelete();
        return executeDropUnit(request, "DELETE", content);
    }

    public Response executeDropUnit(HttpServletRequest request, String method, String content) {
        ReceivedRequest receivedRequest = new ReceivedRequest()
                .withMethod(method)
                .withReceived(content);
        Enumeration<String> headerNames = request.getHeaderNames();
        String name;
        while (headerNames.hasMoreElements()) {
            name = headerNames.nextElement();
            receivedRequest.addHeader(name, request.getHeader(name));
        }
        return processReceivedRequest(request.getPathInfo(), request.getQueryString(), receivedRequest);
    }

    public Response processReceivedRequest(String pathInfo, String queryString, ReceivedRequest receivedRequest) {
        if (receivedRequest == null) {
            LOGGER.warn("'received request' is missing!");
            throw new BadRequestException("'received request' is missing!");
        }
        String url = constructUrl(pathInfo, queryString);
        for (DropUnitEndpoint endpoint : dropUnitService.lookupEndpoint(url, receivedRequest.getMethod())) {
            try {
                LOGGER.debug(endpoint.requestInfoString());
                // valdate if this is the request endpoint to be used
                validator.validate(endpoint, receivedRequest);
                // request received
                endpoint.addReceived(receivedRequest);
                // Response build up
                return generateResponse(endpoint);
            } catch (ValidationException e) {
                LOGGER.info(e.getMessage());
            }
        }
        String msg = String.format("'drop unit '%s' registration is missing!", url);
        LOGGER.warn(msg);
        dropUnitService.registerNotFound(url, receivedRequest);
        throw new NotFoundException(msg);
    }

    private String constructUrl(String pathInfo, String queryString) {
        if ((queryString == null) || queryString.isEmpty()) {
            return pathInfo;
        }
        return pathInfo + '?' + queryString;
    }

    private Response generateResponse(DropUnitEndpoint endpoint) {
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

    private void waitToRespond(int delay) {
        if (0 < delay) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
}
