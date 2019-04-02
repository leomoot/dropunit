package net.lisanza.dropunit.impl.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitEndpointDto {

    @JsonProperty("url")
    private String url;

    @JsonProperty("method")
    private String method;

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

    public int getResponseDelay() {
        return responseDelay;
    }

    public void setResponseDelay(int responseDelay) {
        this.responseDelay = responseDelay;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DropUnitEndpointDto =>\n")
                .append(" url       = '").append(url).append("'\n")
                .append(" method    = '").append(method).append("'\n")
                .append(" resp-Delay= ").append(responseDelay).append("'");
        return builder.toString();
    }
}