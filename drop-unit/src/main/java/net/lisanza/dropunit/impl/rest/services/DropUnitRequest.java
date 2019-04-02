package net.lisanza.dropunit.impl.rest.services;

import static net.lisanza.dropunit.impl.rest.services.DigestUtil.digestRequestBody;

public class DropUnitRequest extends AbstractDropUnitRequest {

    private String requestBody;

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    //

    public DropUnitRequest withRequestBody(String requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    @Override
    public boolean doesRequestMatch(String body) {
        return digestRequestBody(requestBody).equals(digestRequestBody(body));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder()
                .append("DropUnitRequestPatterns =>\n")
                .append(" contentType = '").append(contentType).append("'\n")
                .append(" body= '").append(requestBody).append("''");
        return stringBuilder.toString();
    }
}