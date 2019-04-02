package net.lisanza.dropunit.impl.rest.services;

public class DropUnitResponse {

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

    //

    public DropUnitResponse withCode(int code) {
        this.code = code;
        return this;
    }

    public DropUnitResponse withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public DropUnitResponse withBody(String body) {
        this.body = body;
        return this;
    }

    @Override
    public String toString() {
        return "DropUnitResponse =>\n" +
                " code        = " + code +
                " contentType ='" + contentType + "'\n" +
                " body        ='" + body + "'";
    }
}