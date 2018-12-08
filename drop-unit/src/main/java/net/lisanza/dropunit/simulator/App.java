package net.lisanza.dropunit.simulator;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import net.lisanza.dropunit.simulator.config.yml.AppConfiguration;
import net.lisanza.dropunit.simulator.rest.controlers.DropRegistrationController;
import net.lisanza.dropunit.simulator.health.HealthCheckService;
import net.lisanza.dropunit.simulator.mappers.ExceptionHandler;
import net.lisanza.dropunit.simulator.mappers.ValidationHandler;
import net.lisanza.dropunit.simulator.rest.controlers.DropUnitController;
import net.lisanza.dropunit.simulator.rest.DropUnitCount;
import net.lisanza.dropunit.simulator.rest.services.DropUnitService;
import org.glassfish.jersey.logging.LoggingFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends Application<AppConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        // Start the application
        new App().run(args);
    }

    @Override
    public void run(AppConfiguration configuration, Environment environment) {

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
