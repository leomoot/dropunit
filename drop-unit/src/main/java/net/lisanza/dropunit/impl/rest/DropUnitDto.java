package net.lisanza.dropunit.impl.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DropUnitDto {

    @JsonProperty("url")
    private String url;

    @JsonProperty("method")
    private String method;

    @JsonIgnore
    private DropUnitRequestDto requestBodyInfo;

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

    public void setRequestBodyInfo(DropUnitRequestDto requestBodyInfo) {
        this.requestBodyInfo = requestBodyInfo;
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
            stringBuffer.append(" reqquest  = ").append(requestBodyInfo).append("'\n");
        }
        if (responseBodyInfo != null) {
            stringBuffer.append(" response  = ").append(responseBodyInfo).append("'\n");
        }
        stringBuffer.append(" resp-Delay= ").append(responseDelay).append("'");
        return stringBuffer.toString();
    }
}