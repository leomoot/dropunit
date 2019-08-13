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

    @JsonProperty("request")
    private String request;

    @JsonProperty("requestPattern")
    private List<String> requestPattern;


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

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public List<String> getRequestPattern() {
        return requestPattern;
    }

    public void setRequestPattern(List<String> requestPattern) {
        this.requestPattern = requestPattern;
    }

    @Override
    public String toString() {
        return "DropUnitEndpointDto{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", requestHeaders=" + requestHeaders +
                ", responseDelay=" + responseDelay +
                ", request='" + request + '\'' +
                ", requestPattern=" + requestPattern +
                '}';
    }
}