package net.lisanza.dropunit.impl.rest.services.data;

import net.lisanza.dropunit.impl.rest.dto.DropUnitHeaderDto;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class ReceivedRequest {

    private String method;

    private Map<String, String> headers = new Hashtable<>();

    private String body;

    // getters and setters

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return headers.get("Content-Type");
    }

    // with-builders

    public ReceivedRequest withMethod(String method) {
        this.method = method;
        return this;
    }

    public ReceivedRequest withHeaders(List<DropUnitHeaderDto> headers) {
        for (DropUnitHeaderDto header: headers) {
            this.headers.put(header.getName(), header.getValue());
        }
        return this;
    }

    public ReceivedRequest withReceived(String received) {
        this.body = received;
        return this;
    }

    //

    @Override
    public String toString() {
        return "DropUnitEndpoint => method = '" + method;
    }
}