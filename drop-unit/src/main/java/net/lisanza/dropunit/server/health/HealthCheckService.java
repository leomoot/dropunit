package net.lisanza.dropunit.server.health;

import com.codahale.metrics.health.HealthCheck;

public class HealthCheckService extends HealthCheck {

    @Override
    protected Result check() {
        return Result.unhealthy("No check functions are implemented yet!!!");
    }

}
