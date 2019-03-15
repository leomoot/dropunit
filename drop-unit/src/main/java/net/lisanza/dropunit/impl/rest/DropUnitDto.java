package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitDto {

    @JsonProperty("url")
    private String url;

    @JsonProperty("method")
    private String method;

    @JsonProperty("requestContentType")
    private String requestContentType;

    @JsonProperty("requestBody")
    private String requestBody;

    @JsonProperty("responseCode")
    private int responseCode;

    @JsonProperty("responseContentType")
    private String responseContentType;

    @JsonProperty("responseBody")
    private String responseBody;

    @JsonProperty("responseDelay")
    private int responseDelay;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

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

    public int getResponseDelay() {
        return responseDelay;
    }

    public void setResponseDelay(int responseDelay) {
        this.responseDelay = responseDelay;
    }

    @Override
    public String toString() {
        return "DropUnitDto =>\n" +
                " url             = '" + url + "'\n" +
                " method          = '" + method + "'\n" +
                " req-ContentType = '" + requestContentType + "'\n" +
                " req-Body        = '" + requestBody + "'\n" +
                " resp-Code       = " + responseCode +
                " resp-ContentType='" + responseContentType + "'\n" +
                " resp-Body       ='" + responseBody + "'\n" +
                " resp-Delay      =" + responseDelay + "'";
    }
}