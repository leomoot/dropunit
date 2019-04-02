package net.lisanza.dropunit.impl.rest.controlers;

import net.lisanza.dropunit.impl.rest.DropUnitEndpointCountDto;
import net.lisanza.dropunit.impl.rest.DropUnitEndpointRegistrationDto;
import net.lisanza.dropunit.impl.rest.DropUnitEndpointUpdateDto;
import net.lisanza.dropunit.impl.rest.constants.RequestMappings;
import net.lisanza.dropunit.impl.rest.dto.DropUnitEndpointDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitRequestPatternsDto;
import net.lisanza.dropunit.impl.rest.services.DropUnitCount;
import net.lisanza.dropunit.impl.rest.services.DropUnitRequest;
import net.lisanza.dropunit.impl.rest.services.DropUnitRequestPatterns;
import net.lisanza.dropunit.impl.rest.services.DropUnitResponse;
import net.lisanza.dropunit.impl.rest.services.DropUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON) // we produce always JSON. We might read other media types.
@Path(RequestMappings.DROP_UNIT_SERVICE)
public class DropRegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropRegistrationController.class);

    private final DropUnitCount dropUnitCount;
    private final DropUnitService dropUnitService;

    public DropRegistrationController(DropUnitService dropUnitService,
                                      DropUnitCount dropUnitCount) {
        this.dropUnitService = dropUnitService;
        this.dropUnitCount = dropUnitCount;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delivery/endpoint")
    public DropUnitEndpointRegistrationDto registerEndpoint(@Valid DropUnitEndpointDto dto) {
        try {
            LOGGER.debug("Called registerEndpoint");
            return new DropUnitEndpointRegistrationDto()
                    .withResult("OK")
                    .withId(dropUnitService.register(new net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint()
                            .withUrl(dto.getUrl())
                            .withMethod(dto.getMethod())
                            .withDelay(dto.getResponseDelay())));
        } catch (Exception e) {
            LOGGER.warn("Failure in registration of endpoint", e);
        }
        throw new InternalServerErrorException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delivery/endpoint/{dropId}/request-body")
    public DropUnitEndpointUpdateDto registerRequestPatterns(@PathParam("dropId") String dropId,
                                                             @Valid DropUnitRequestPatternsDto requestDto) {
        try {
            LOGGER.debug("Called registerRequestPatterns {}", dropId);
            return new DropUnitEndpointUpdateDto()
                    .withResult(dropUnitService.registerRequest(dropId, new DropUnitRequestPatterns()
                            .withPatterns(requestDto.getPatterns())
                            .withContentType(requestDto.getRequestContentType())));
        } catch (Exception e) {
            LOGGER.warn("Failure generating response registerDropUnitRequestBody", e);
        }
        throw new InternalServerErrorException();
    }

    @PUT
    @Path("/delivery/endpoint/{dropId}/request-body")
    public DropUnitEndpointUpdateDto registerRequestBody(@Context HttpServletRequest request,
                                                         @PathParam("dropId") String dropId,
                                                         String requestBody) {
        try {
            LOGGER.debug("Called registerRequestBody {}", dropId);
            return new DropUnitEndpointUpdateDto()
                    .withResult(dropUnitService.registerRequest(dropId, new DropUnitRequest()
                            .withRequestBody(requestBody)
                            .withContentType(request.getContentType())));
        } catch (Exception e) {
            LOGGER.warn("Failure generating response registerDropUnitRequestBody", e);
        }
        throw new InternalServerErrorException();
    }

    @PUT
    @Path("/delivery/endpoint/{dropId}/response-body/{status}")
    public DropUnitEndpointUpdateDto registerResponseBody(@Context HttpServletRequest request,
                                                          @PathParam("dropId") String dropId,
                                                          @PathParam("status") int status,
                                                          String responseBody) {
        try {
            LOGGER.debug("Called registerResponseBody {}", dropId);
            return new DropUnitEndpointUpdateDto()
                    .withResult(dropUnitService.registerResponse(dropId, new DropUnitResponse()
                            .withCode(status)
                            .withBody(responseBody)
                            .withContentType(request.getContentType())));
        } catch (Exception e) {
            LOGGER.warn("Failure generating response putDropUnitResponseBody", e);
        }
        throw new InternalServerErrorException();
    }

    @GET
    @Path("/clearAllDrop")
    public String clearAllDrop() {
        LOGGER.debug("Called clearAllDrop");
        return dropUnitService.dropAll();
    }

    @GET
    @Path("/getAllDrop")
    public List<net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint> getAllDrop() {
        try {
            LOGGER.debug("Called getAllDrop");
            return dropUnitService.getAll();
        } catch (Exception e) {
            LOGGER.warn("Failure generating response getAllDrop", e);
        }
        throw new InternalServerErrorException();
    }

    @GET
    @Path("/getDropCount/{dropUnitId}")
    public DropUnitEndpointCountDto getDropCount(@PathParam("dropUnitId") String dropUnitId) {
        try {
            LOGGER.debug("Called getDropCount");
            net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint endpoint = dropUnitService.lookupEndpoint(dropUnitId);
            return new DropUnitEndpointCountDto()
                    .withCount(endpoint.getCount());
        } catch (Exception e) {
            LOGGER.warn("Failure generating response getDropCount", e);
        }
        throw new InternalServerErrorException();
    }

    @DELETE
    @Path("/delivery/endpoint/{dropUnitId}")
    public DropUnitEndpointCountDto deleteEndpoint(@PathParam("dropUnitId") String dropUnitId) {
        try {
            LOGGER.debug("Called getDropCount");
            net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint endpoint = dropUnitService.deregister(dropUnitId);
            return new DropUnitEndpointCountDto()
                    .withCount(endpoint.getCount());
        } catch (Exception e) {
            LOGGER.warn("Failure generating response getDropCount", e);
        }
        throw new InternalServerErrorException();
    }
}
