package net.lisanza.dropunit.server.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitRequestDto {

    @JsonProperty("requestContentType")
    protected String requestContentType;

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
        return "DropUnitRequestDto =>\n" +
                " req-ContentType = '" + requestContentType + "'\n" +
                " req-Body        = '" + requestBody + "'";
    }
}