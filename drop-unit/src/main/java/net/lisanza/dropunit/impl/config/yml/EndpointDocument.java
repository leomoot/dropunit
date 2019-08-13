package net.lisanza.dropunit.impl.config.yml;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.lisanza.dropunit.impl.rest.services.DropUnitEndpointRequest;

import javax.validation.constraints.NotNull;

public class EndpointDocument {

    /**
     * The URL path for which this endpoint.
     */
    @NotNull
    private String path;

    /**
     * The HTTP method to which this endpoint will react.
     */
    @NotNull
    private String method;

    /**
     * The delay in lillis beofre the response is returned.
     */
    private int delay;

    /**
     * Patterns that must be matched in the request.
     */
    private DropUnitEndpointRequest request;

    /**
     * The HTTP response code with which this endpoint will react.
     */
    @NotNull
    private int responseCode;

    /**
     * The content-type with which this endpoint will react in the response.
     */
    @NotNull
    private String responseContentType;

    /**
     * The response body with which this endpoint will react.
     */
    @NotNull
    private String responseBodyFileName;

    @JsonProperty
    public String getPath() {
        return path;
    }

    @JsonProperty
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty
    public String getMethod() {
        return method;
    }

    @JsonProperty
    public int getDelay() {
        return delay;
    }

    @JsonProperty
    public void setDelay(int delay) {
        this.delay = delay;
    }

    @JsonProperty
    public DropUnitEndpointRequest getRequest() {
        return request;
    }

    @JsonProperty
    public void setRequest(DropUnitEndpointRequest request) {
        this.request = request;
    }

    @JsonProperty
    public void setMethod(String method) {
        this.method = method;
    }

    @JsonProperty
    public int getResponseCode() {
        return responseCode;
    }

    @JsonProperty
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @JsonProperty
    public String getResponseContentType() {
        return responseContentType;
    }

    @JsonProperty
    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    @JsonProperty
    public String getResponseBodyFileName() {
        if ((responseBodyFileName == null) || responseBodyFileName.isEmpty()) {
            return null;
        }
        if ((System.getenv("PREFIX") == null) || System.getenv("PREFIX").isEmpty()) {
            return responseBodyFileName;
        } else {
            return System.getenv("PREFIX") + responseBodyFileName;
        }
    }

    @JsonProperty
    public void setResponseBodyFileName(String responseBodyFileName) {
        this.responseBodyFileName = responseBodyFileName;
    }

    @Override
    public String toString() {
        return "{ path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", delay='" + delay + '\'' +
                ", responseCode=" + responseCode +
                ", responseContentType='" + responseContentType + '\'' +
                ", responseBodyFileName='" + responseBodyFileName + '\'' +
                '}';
    }
}