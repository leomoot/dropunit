package net.lisanza.dropunit.impl.config.yml;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class EndpointDocument {

    @NotNull
    private String path;

    @NotNull
    private String method;

    @NotNull
    private int responseCode;

    @NotNull
    private String responseContentType;

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
                ", responseCode=" + responseCode +
                ", responseContentType='" + responseContentType + '\'' +
                ", responseBodyFileName='" + responseBodyFileName + '\'' +
                '}';
    }
}