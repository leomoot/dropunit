package net.lisanza.dropunit.impl.rest.controlers;

import net.lisanza.dropunit.impl.rest.DropUnitCount;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.impl.rest.DropUnitEndpointCountDto;
import net.lisanza.dropunit.impl.rest.DropUnitEndpointRegistrationDto;
import net.lisanza.dropunit.impl.rest.DropUnitEndpointUpdateDto;
import net.lisanza.dropunit.impl.rest.DropUnitRequestDto;
import net.lisanza.dropunit.impl.rest.DropUnitRequestPatternsDto;
import net.lisanza.dropunit.impl.rest.DropUnitResponseDto;
import net.lisanza.dropunit.impl.rest.constants.RequestMappings;
import net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint;
import net.lisanza.dropunit.impl.rest.services.DropUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
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
    public DropUnitEndpointRegistrationDto registerEndpoint(@Valid DropUnitDto dto) {
        try {
            LOGGER.debug("Called registerEndpoint");
            return new DropUnitEndpointRegistrationDto()
                    .withResult("OK")
                    .withId(dropUnitService.register(dto));
        } catch (Exception e) {
            LOGGER.warn("Failure in registration of endpoint", e);
        }
        throw new InternalServerErrorException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delivery/endpoint/{dropId}/request-body")
    public DropUnitEndpointUpdateDto registerRequestBody(@PathParam("dropId") String dropId,
                                                         @Valid DropUnitRequestPatternsDto requestDto) {
        try {
            LOGGER.debug("Called registerRequestBody {}", dropId);
            DropUnitEndpoint endpoint = dropUnitService.lookupEndpoint(dropId);
            if (endpoint == null) {
                return new DropUnitEndpointUpdateDto()
                        .withResult("no registration");
            }
            endpoint.getDropUnitDto().setRequestBodyPatterns(requestDto);
            return new DropUnitEndpointUpdateDto().withResult("OK");
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
            DropUnitEndpoint endpoint = dropUnitService.lookupEndpoint(dropId);
            if (endpoint == null) {
                return new DropUnitEndpointUpdateDto()
                        .withResult("no registration");
            }
            DropUnitRequestDto requestDto = new DropUnitRequestDto();
            requestDto.setRequestContentType(request.getContentType());
            requestDto.setRequestBody(requestBody);
            endpoint.getDropUnitDto().setRequestBodyInfo(requestDto);
            return new DropUnitEndpointUpdateDto().withResult("OK");
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
            DropUnitEndpoint endpoint = dropUnitService.lookupEndpoint(dropId);
            if (endpoint == null) {
                return new DropUnitEndpointUpdateDto()
                        .withResult("no registration");
            }
            DropUnitResponseDto responseDto = new DropUnitResponseDto();
            responseDto.setResponseContentType(request.getContentType());
            responseDto.setResponseBody(responseBody);
            responseDto.setResponseCode(status);
            endpoint.getDropUnitDto().setResponseBodyInfo(responseDto);
            return new DropUnitEndpointUpdateDto()
                    .withResult("OK");
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
    public List<DropUnitDto> getAllDrop() {
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
            DropUnitEndpoint endpoint = dropUnitService.lookupEndpoint(dropUnitId);
            return new DropUnitEndpointCountDto()
                    .withCount(endpoint.getCount());
        } catch (Exception e) {
            LOGGER.warn("Failure generating response getDropCount", e);
        }
        throw new InternalServerErrorException();
    }
}
