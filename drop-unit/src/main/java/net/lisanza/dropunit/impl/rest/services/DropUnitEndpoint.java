package net.lisanza.dropunit.impl.rest.services;

import java.util.ArrayList;
import java.util.List;

public class DropUnitEndpoint {

    private String id;

    private String url;

    private String method;

    private int delay;

    private int count;

    private AbstractDropUnitRequest request;

    private DropUnitResponse response;

    private List<String> receivedList = new ArrayList<>();

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

    public String getReceived(int i) {
        if ((0 < i) && (i <= receivedList.size())) {
            return receivedList.get(i - 1);
        }
        return "";
    }

    public void addReceived(String received) {
        this.receivedList.add(received);
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

    //

    @Override
    public String toString() {
        return "DropUnitEndpoint => id = '" + id + "'\n" +
                " url    = '" + url + "'\n" +
                " method = '" + method + "'\n" +
                " delay  = " + delay + "'\n" +
                " count  = " + count;
    }
}
