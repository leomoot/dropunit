package net.lisanza.dropunit.impl.rest.services;

import net.lisanza.dropunit.impl.rest.dto.DropUnitHeaderDto;
import net.lisanza.dropunit.impl.rest.services.data.ReceivedRequest;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DropUnitEndpoint {

    private String id;

    private String url;

    private String method;

    private Map<String, String> headers = new Hashtable<>();

    private int delay;

    private int count;

    private AbstractDropUnitRequest request;

    private DropUnitResponse response;

    private List<ReceivedRequest> receivedList = new ArrayList<>();

    // getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void incr() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public AbstractDropUnitRequest getRequest() {
        return request;
    }

    public void setRequest(AbstractDropUnitRequest request) {
        this.request = request;
    }

    public DropUnitResponse getResponse() {
        return response;
    }

    public void setResponse(DropUnitResponse response) {
        this.response = response;
    }

    public int getReceivedSize() {
        return receivedList.size();
    }

    public ReceivedRequest getReceived(int i) {
        if ((0 < i) && (i <= receivedList.size())) {
            return receivedList.get(i - 1);
        }
        return null;
    }

    public void addReceived(ReceivedRequest received) {
        this.receivedList.add(received);
        incr();
    }

    // with-builders

    public DropUnitEndpoint withId(String id) {
        this.id = id;
        return this;
    }

    public DropUnitEndpoint withUrl(String url) {
        this.url = url;
        return this;
    }

    public DropUnitEndpoint withMethod(String method) {
        this.method = method;
        return this;
    }

    public DropUnitEndpoint withHeaders(List<DropUnitHeaderDto> headers) {
        for (DropUnitHeaderDto header: headers) {
            this.headers.put(header.getName(), header.getValue());
        }
        return this;
    }

    public DropUnitEndpoint withDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public DropUnitEndpoint withRequest(AbstractDropUnitRequest request) {
        this.request = request;
        return this;
    }

    public DropUnitEndpoint withResponse(DropUnitResponse response) {
        this.response = response;
        return this;
    }

    // toString

    public String requestInfoString() {
        return "DropUnitEndpoint{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", request=" + request +
                '}';
    }

    @Override
    public String toString() {
        return "DropUnitEndpoint => id = '" + id + "'\n" +
                " url    = '" + url + "'\n" +
                " method = '" + method + "'\n" +
                " delay  = " + delay + "'\n" +
                " count  = " + count;
    }
}
