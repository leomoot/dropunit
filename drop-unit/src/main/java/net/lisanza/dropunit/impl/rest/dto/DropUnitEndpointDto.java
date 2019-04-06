package net.lisanza.dropunit.impl.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DropUnitEndpointDto {

    @JsonProperty("url")
    private String url;

    @JsonProperty("method")
    private String method;

    @JsonProperty("requestHeaders")
    private List<DropUnitHeaderDto> requestHeaders = new ArrayList();

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

    public List<DropUnitHeaderDto> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(List<DropUnitHeaderDto> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public void addRequestHeader(DropUnitHeaderDto requestHeader) {
        this.requestHeaders.add(requestHeader);
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