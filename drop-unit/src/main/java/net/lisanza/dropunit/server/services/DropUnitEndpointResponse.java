package net.lisanza.dropunit.server.services;

import net.lisanza.dropunit.server.rest.dto.DropUnitHeaderDto;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropUnitEndpointResponse {

    private int code;

    private Map<String, String> headers;

    private String contentType;

    private String body;

    // Getters and Setters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    // Builders

    public DropUnitEndpointResponse withCode(int code) {
        this.code = code;
        return this;
    }

    public DropUnitEndpointResponse withHeaders(List<DropUnitHeaderDto> headers) {
        this.headers = new HashMap<>();
        if (headers != null) {
            for (DropUnitHeaderDto hdr: headers) {
                this.headers.put(hdr.getName(), hdr.getValue());
            }
        }
        return this;
    }

    public DropUnitEndpointResponse withHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public DropUnitEndpointResponse withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public DropUnitEndpointResponse withBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "DropUnitEndpointResponse =>\n" +
                " code        = " + code + "\n" +
                " headers    = " + toStringHeaders() + "\n" +
                " contentType ='" + contentType + "'\n" +
                " body        ='" + body + "'";
    }

    private String toStringHeaders() {
        if (headers != null) {
            StringBuffer stringBuffer = new StringBuffer();
            for (String hdrKey : headers.keySet()) {
                stringBuffer.append('\'')
                        .append(hdrKey).append(": ")
                        .append(headers.get(hdrKey))
                        .append('\'').append(',');
            }
            return stringBuffer.toString();
        }
        return "''";
    }

    // hashCode

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(code)
                .append(headers)
                .append(contentType)
                .append(body)
                .toHashCode();
    }

}