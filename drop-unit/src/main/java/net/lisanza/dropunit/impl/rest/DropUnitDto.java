package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.naming.CannotProceedException;

public class DropUnitDto {

    @JsonProperty("url")
    private String url;

    @JsonProperty("method")
    private String method;

    @JsonIgnore
    private DropUnitRequestDto requestBodyInfo;

    @JsonIgnore
    private DropUnitRequestPatternsDto requestBodyPatterns;

    @JsonIgnore
    private DropUnitResponseDto responseBodyInfo;

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

    public DropUnitRequestDto getRequestBodyInfo() {
        return requestBodyInfo;
    }

    public void setRequestBodyInfo(DropUnitRequestDto requestBodyInfo)
            throws CannotProceedException {
        if (this.requestBodyPatterns != null) {
            throw new CannotProceedException("request body patterns already set");
        }
        this.requestBodyInfo = requestBodyInfo;
    }

    public DropUnitRequestPatternsDto getRequestBodyPatterns() {
        return requestBodyPatterns;
    }

    public void setRequestBodyPatterns(DropUnitRequestPatternsDto requestBodyPatterns)
            throws CannotProceedException {
        if (this.requestBodyInfo != null) {
            throw new CannotProceedException("request body info");
        }
        this.requestBodyPatterns = requestBodyPatterns;
    }

    public DropUnitResponseDto getResponseBodyInfo() {
        return responseBodyInfo;
    }

    public void setResponseBodyInfo(DropUnitResponseDto responseBodyInfo) {
        this.responseBodyInfo = responseBodyInfo;
    }

    public int getResponseDelay() {
        return responseDelay;
    }

    public void setResponseDelay(int responseDelay) {
        this.responseDelay = responseDelay;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DropUnitDto =>\n")
                .append(" url       = '").append(url).append("'\n")
                .append(" method    = '").append(method).append("'\n");
        if (responseBodyInfo != null) {
            stringBuffer.append(" request   = ").append(requestBodyInfo).append("'\n");
        }
        if (requestBodyPatterns != null) {
            stringBuffer.append(" request   = ").append(requestBodyPatterns).append("'\n");
        }
        if (responseBodyInfo != null) {
            stringBuffer.append(" response  = ").append(responseBodyInfo).append("'\n");
        }
        stringBuffer.append(" resp-Delay= ").append(responseDelay).append("'");
        return stringBuffer.toString();
    }
}