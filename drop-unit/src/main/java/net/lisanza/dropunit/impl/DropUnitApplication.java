package net.lisanza.dropunit.impl;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import net.lisanza.dropunit.impl.config.yml.DropUnitConfiguration;
import net.lisanza.dropunit.impl.config.yml.EndpointDocument;
import net.lisanza.dropunit.impl.health.HealthCheckService;
import net.lisanza.dropunit.impl.mappers.ExceptionHandler;
import net.lisanza.dropunit.impl.mappers.ValidationHandler;
import net.lisanza.dropunit.impl.rest.controlers.DropRegistrationController;
import net.lisanza.dropunit.impl.rest.controlers.DropUnitController;
import net.lisanza.dropunit.impl.rest.services.DropUnitCount;
import net.lisanza.dropunit.impl.rest.services.DropUnitEndpoint;
import net.lisanza.dropunit.impl.rest.services.DropUnitResponse;
import net.lisanza.dropunit.impl.rest.services.DropUnitService;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static net.lisanza.dropunit.impl.utils.FileUtils.readFromFile;

public class DropUnitApplication<TypeOfConfiguration extends DropUnitConfiguration> extends Application<TypeOfConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropUnitApplication.class);

    @Override
    public void run(TypeOfConfiguration configuration, Environment environment) {
        // Setup dropunit service
        DropUnitCount dropUnitCount = new DropUnitCount();
        DropUnitService dropUnitService = new DropUnitService();

        // handle initial endpoints
        initConfig(configuration, dropUnitService);

        // Registration of the handlers / mappings
        environment.jersey().register(new ExceptionHandler());
        environment.jersey().register(new ValidationHandler());

        // Logging inbound request/response
        environment.jersey().register(new LoggingFeature(java.util.logging.Logger.getLogger("net.lisanza.dropunit.simulator.logging"),
                LoggingFeature.Verbosity.PAYLOAD_ANY));

        // Registration of the REST controllers
        environment.jersey().register(new DropUnitController(dropUnitService, dropUnitCount));
        environment.jersey().register(new DropRegistrationController(dropUnitService, dropUnitCount));

        // Registration of the required Dropwizard health checks
        environment.healthChecks().register("HEALTH", new HealthCheckService());
    }

    protected void initConfig(TypeOfConfiguration config,
                              DropUnitService dropUnitService) {
        for (EndpointDocument endpointDocument : config.getEndpoints()) {
            configEndpoint(endpointDocument, dropUnitService);
        }
        dropUnitService.dropAll();
    }

    private void configEndpoint(EndpointDocument endpointDocument,
                                DropUnitService dropUnitService) {
        try {
            DropUnitResponse response = new DropUnitResponse()
                    .withCode(endpointDocument.getResponseCode())
                    .withContentType(endpointDocument.getResponseContentType())
                    .withBody("");
            if ((endpointDocument.getResponseBodyFileName() != null) && !endpointDocument.getResponseBodyFileName().isEmpty()) {
                response.withBody(readFromFile(endpointDocument.getResponseBodyFileName()));
            }
            LOGGER.debug("response: {}", response);
            dropUnitService.registerDefault(new DropUnitEndpoint()
                    .withUrl(endpointDocument.getPath())
                    .withMethod(endpointDocument.getMethod())
                    .withRequest(endpointDocument.getRequest())
                    .withResponse(response));
            LOGGER.debug("response: {}", response);
        } catch (NullPointerException e) {
            LOGGER.error("Cannot load endpoint: {} / {}: {}",
                    endpointDocument.getPath(), endpointDocument.getMethod(), e.getMessage());
            return;
        } catch (IOException e) {
            LOGGER.error("Cannot read endpoint {} / {} response body filename: {}",
                    endpointDocument.getPath(), endpointDocument.getMethod(),
                    endpointDocument.getResponseBodyFileName());
            LOGGER.warn("", e);
            return;
        }
        LOGGER.info("Loaded endpoint: {}", endpointDocument.toString());
    }
}
