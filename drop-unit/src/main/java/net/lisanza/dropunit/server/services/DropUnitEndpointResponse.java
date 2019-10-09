package net.lisanza.dropunit.server.services;

import org.apache.commons.lang3.builder.HashCodeBuilder;

public class DropUnitEndpointResponse {

    private int code;

    private String contentType;

    private String body;

    // Getters and Setters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
                " code        = " + code +
                " contentType ='" + contentType + "'\n" +
                " body        ='" + body + "'";
    }

    // hashCode

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(code)
                .append(contentType)
                .append(body)
                .toHashCode();
    }
}