package net.lisanza.dropunit.impl.rest.services.data;

import java.util.ArrayList;
import java.util.List;

public class EndpointNotFound {

    private String url;

    private List<ReceivedRequest> receivedRequests = new ArrayList<>();

    // getters and setters

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ReceivedRequest getReceivedRequests(int i) {
        if ((0 < i) && (i <= receivedRequests.size())) {
            return receivedRequests.get(i - 1);
        }
        return null;
    }

    public void addReceivedRequests(ReceivedRequest received) {
        this.receivedRequests.add(received);
    }

    public int getCount() {
        return receivedRequests.size();
    }

    // with-builders

    public EndpointNotFound withUrl(String url) {
        this.url = url;
        return this;
    }

    public EndpointNotFound withReceivedRequests(ReceivedRequest notFoundRequest) {
        this.receivedRequests.add(notFoundRequest);
        return this;
    }

    //

    @Override
    public String toString() {
        return "DropUnitEndpoint => url    = '" + url + "'\n" +
                " count  = " + getCount();
    }
}
