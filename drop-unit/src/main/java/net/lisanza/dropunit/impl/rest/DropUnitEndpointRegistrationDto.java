package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitEndpointRegistrationDto {

    @JsonProperty("result")
    private String result;

    @JsonProperty("id")
    private String id;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DropUnitEndpointRegistrationDto withResult(String result) {
        this.result = result;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DropUnitEndpointRegistrationDto withId(String id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "EndpointRegistration =>\n" +
                " result      = '" + result + "'\n" +
                " id          = '" + id + "'";
    }
}