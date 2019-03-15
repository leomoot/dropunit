package net.lisanza.dropunit.engineundertest;


import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import net.lisanza.dropunit.engineundertest.config.yml.AppConfiguration;
import net.lisanza.dropunit.engineundertest.controller.ProxyController;
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
    public void run(AppConfiguration configuration, Environment environment) throws Exception {

        // Logging inbound request/response
        environment.jersey().register(new LoggingFeature(java.util.logging.Logger.getLogger("net.lisanza.dropunit.engineundertest.logging"),
                LoggingFeature.Verbosity.PAYLOAD_ANY));

        // Registration of the REST controllers
        environment.jersey().register(new ProxyController(configuration.getProxyUrl()));

        // Registration of the required Dropwizard health checks
        environment.healthChecks().register("HEALTH", new HealthCheck(){
            @Override
            protected Result check() throws Exception {
                return Result.unhealthy("No check functions are implemented yet!!!");
            }
        });

    }
}
