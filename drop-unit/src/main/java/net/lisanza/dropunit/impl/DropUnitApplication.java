package net.lisanza.dropunit.impl;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import net.lisanza.dropunit.impl.health.HealthCheckService;
import net.lisanza.dropunit.impl.mappers.ExceptionHandler;
import net.lisanza.dropunit.impl.mappers.ValidationHandler;
import net.lisanza.dropunit.impl.rest.controlers.DropRegistrationController;
import net.lisanza.dropunit.impl.rest.controlers.DropUnitController;
import net.lisanza.dropunit.impl.rest.services.DropUnitCount;
import net.lisanza.dropunit.impl.rest.services.DropUnitService;
import org.glassfish.jersey.logging.LoggingFeature;

public class DropUnitApplication<TypeOfConfiguration extends Configuration> extends Application<TypeOfConfiguration> {

    @Override
    public void run(TypeOfConfiguration configuration, Environment environment) {

        // Registration of the handlers / mappings
        environment.jersey().register(new ExceptionHandler());
        environment.jersey().register(new ValidationHandler());

        // Logging inbound request/response
        environment.jersey().register(new LoggingFeature(java.util.logging.Logger.getLogger("net.lisanza.dropunit.simulator.logging"),
                LoggingFeature.Verbosity.PAYLOAD_ANY));

        // Registration of the REST controllers
        DropUnitCount dropUnitCount = new DropUnitCount();
        DropUnitService dropUnitService = new DropUnitService();
        environment.jersey().register(new DropUnitController(dropUnitService, dropUnitCount));
        environment.jersey().register(new DropRegistrationController(dropUnitService, dropUnitCount));

        // Registration of the required Dropwizard health checks
        environment.healthChecks().register("HEALTH", new HealthCheckService());

    }
}
