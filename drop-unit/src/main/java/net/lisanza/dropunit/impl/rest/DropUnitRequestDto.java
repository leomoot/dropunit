package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitRequestDto extends AbstractDropUnitRequestDto {

    @JsonProperty("requestBody")
    private String requestBody;

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