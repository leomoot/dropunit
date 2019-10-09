package net.lisanza.dropunit.server.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitResponseDto {

    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("responseContentType")
    private String responseContentType;

    @JsonProperty("responseBody")
    private String responseBody;

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "DropUnitResponseDto =>\n" +
                " resp-Code       = " + responseCode +
                " resp-ContentType='" + responseContentType + "'\n" +
                " resp-Body       ='" + responseBody + "'";
    }
}