package net.lisanza.dropunit.impl.rest.controlers;

import net.lisanza.dropunit.impl.rest.DropUnitCount;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.impl.rest.constants.RequestMappings;
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
            endpoint = lookup(createDropUnit(request.getPathInfo(), method, content));
        } else {
            endpoint = lookup(createDropUnit(request.getPathInfo() + "?" + request.getQueryString(), method, content));
        }
        endpoint.incr();
        DropUnitDto result = endpoint.getDropUnitDto();
        validateRequestContentType(result, request);
        // Response build up
        waitToRespond(result);
        Response.ResponseBuilder responseBuilder = buildResponse(result);
        addContentType(result, responseBuilder);
        addContent(result, responseBuilder);
        return responseBuilder.build();
    }

    private Response.ResponseBuilder buildResponse(DropUnitDto dropUnitDto) {
        LOGGER.info("response code: {}", dropUnitDto.getResponseBodyInfo().getResponseCode());
        return Response.status(Response.Status.fromStatusCode(dropUnitDto.getResponseBodyInfo().getResponseCode()));
    }

    private void addContentType(DropUnitDto dropUnitDto, Response.ResponseBuilder responseBuilder) {
        if (dropUnitDto.getResponseBodyInfo().getResponseContentType() == null) {
            responseBuilder.header("Content-type", dropUnitDto.getResponseBodyInfo().getResponseContentType());
        }
    }

    private void addContent(DropUnitDto dropUnitDto, Response.ResponseBuilder responseBuilder) {
        if (dropUnitDto.getResponseBodyInfo().getResponseBody() != null) {
            responseBuilder.entity(dropUnitDto.getResponseBodyInfo().getResponseBody());
        }
    }

    private void validateRequestContentType(DropUnitDto dropUnitDto, HttpServletRequest request) {
        if ((request.getHeader("Content-type") != null) &&
                (dropUnitDto.getRequestBodyInfo().getRequestContentType() != null) &&
                (!dropUnitDto.getRequestBodyInfo().getRequestContentType().equals(request.getHeader("Content-type")))) {
            throw new NotSupportedException();
        }
    }

    private void waitToRespond(DropUnitDto dropUnitDto) {
        if (0 < dropUnitDto.getResponseDelay()) {
            try {
                Thread.sleep(dropUnitDto.getResponseDelay());
            } catch (InterruptedException e) {

            }
        }
    }

    private DropUnitEndpoint lookup(DropUnitDto dropUnitDto) {
        DropUnitEndpoint result = dropUnitService.lookupEndpoint(dropUnitDto);
        if (result == null) {
            String msg = String.format("'drop unit '%s' registration is missing!", dropUnitDto.getUrl());
            LOGGER.error(msg);
            throw new NotFoundException(msg);
        }

        LOGGER.info("lookupEndpoint -> {}", result);
        return result;
    }

    public DropUnitDto createDropUnit(String uri, String method, String requestBody) {
        DropUnitDto dropUnitDto = new DropUnitDto();
        dropUnitDto.setUrl(uri);
        dropUnitDto.setMethod(method);
        return dropUnitDto;
    }
}
