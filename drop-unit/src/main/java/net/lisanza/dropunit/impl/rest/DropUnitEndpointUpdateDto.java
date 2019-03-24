package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitEndpointUpdateDto {

    @JsonProperty("result")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public DropUnitEndpointUpdateDto withResult(String result) {
        this.result = result;
        return this;
    }

    @Override
    public String toString() {
        return "EndpointUpdate =>\n" +
                " result      = '" + result + "'";
    }
}