package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitRequestDto {

    @JsonProperty("requestContentType")
    private String requestContentType;

    @JsonProperty("requestBody")
    private String requestBody;

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public String toString() {
        return "DropUnitDto =>\n" +
                " req-ContentType = '" + requestContentType + "'\n" +
                " req-Body        = '" + requestBody + "'";
    }
}