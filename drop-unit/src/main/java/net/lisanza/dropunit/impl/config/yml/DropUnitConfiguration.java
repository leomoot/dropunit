package net.lisanza.dropunit.impl.config.yml;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;
import java.util.List;

public class DropUnitConfiguration extends Configuration {

    @NotNull
    private List<EndpointDocument> endpoints;

    @JsonProperty
    public List<EndpointDocument> getEndpoints() {
        return endpoints;
    }

    @JsonProperty
    public void setEndpoints(List<EndpointDocument> endpoints) {
        this.endpoints = endpoints;
    }

}
