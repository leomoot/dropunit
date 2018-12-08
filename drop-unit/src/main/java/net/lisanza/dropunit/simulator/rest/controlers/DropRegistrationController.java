package net.lisanza.dropunit.simulator.rest.controlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lisanza.dropunit.simulator.rest.DropUnitDto;
import net.lisanza.dropunit.simulator.rest.constants.RequestMappings;
import net.lisanza.dropunit.simulator.rest.DropUnitCount;
import net.lisanza.dropunit.simulator.rest.services.DropUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
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
    @Path("/delivery/{dropId}")
    public String postDropUnit(@PathParam ("dropId") String dropId,
                               @Valid DropUnitDto dto) {
        LOGGER.info("Called postDropUnit");
        return dropUnitService.register(dropId, dto);
    }

    @GET
    @Path("/clearAllDrop")
    public String clearAllDrop() {
        LOGGER.info("Called clearAllDrop");
        return dropUnitService.dropAll();
    }

    @GET
    @Path("/getAllDrop")
    public String getAllDrop() {
        try {
            LOGGER.info("Called getAllDrop");
            return new ObjectMapper().writeValueAsString(dropUnitService.getAll());
        } catch (JsonProcessingException e) {
            LOGGER.info("Failure generating DropUnitDto list", e);
        }
        throw new InternalServerErrorException();
    }

    @GET
    @Path("/getDropCount")
    public String getDropCount() {
        try {
            LOGGER.info("Called getDropCount");
            return new ObjectMapper().writeValueAsString(dropUnitCount);
        } catch (JsonProcessingException e) {
            LOGGER.info("Failure generating DropUnitDto list", e);
        }
        throw new InternalServerErrorException();
    }
}
