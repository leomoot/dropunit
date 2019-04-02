package net.lisanza.dropunit.impl.rest.services;

public abstract class AbstractDropUnitRequest {

    protected String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public AbstractDropUnitRequest withContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public abstract boolean doesRequestMatch(String body);
}
