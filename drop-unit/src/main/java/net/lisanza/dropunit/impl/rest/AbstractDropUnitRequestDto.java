package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class AbstractDropUnitRequestDto {

    @JsonProperty("requestContentType")
    protected String requestContentType;

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }
}