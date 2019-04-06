package net.lisanza.dropunit.client;

import net.lisanza.dropunit.impl.rest.dto.DropUnitEndpointDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitRequestDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitRequestPatternsDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitResponseDto;

import javax.naming.CannotProceedException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ClientDropUnit extends BaseDropUnitClient {

    private String id;

    private int count;

    private DropUnitEndpointDto dropUnitEndpointDto = null;
    private DropUnitRequestDto requestBodyInfo = null;
    private DropUnitRequestPatternsDto requestPatterns = null;
    private DropUnitResponseDto responseDto = null;

    // Constructor

    public ClientDropUnit(final String baseUrl) {
        super(baseUrl);
    }

    // Getters & Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientDropUnit withId(String id) {
        this.id = id;
        return this;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ClientDropUnit withCount(int count) {
        this.count = count;
        return this;
    }

    // Delegates

    public String getUrl()
            throws CannotProceedException {
        if (this.dropUnitEndpointDto == null) {
            throw new CannotProceedException("withEndpoint is not called before");
        }
        return dropUnitEndpointDto.getUrl();
    }

    public String getResponseBody() {
        return this.responseDto.getResponseBody();
    }

    // Operations

    public ClientDropUnit withGet(String uri) {
        return withEndpoint(uri, "GET");
    }

    public ClientDropUnit withPost(String uri) {
        return withEndpoint(uri, "POST");
    }

    public ClientDropUnit withPut(String uri) {
        return withEndpoint(uri, "PUT");
    }

    public ClientDropUnit withDelete(String uri) {
        return withEndpoint(uri, "DELETE");
    }

    public ClientDropUnit withEndpoint(String uri, String method) {
        this.dropUnitEndpointDto = new DropUnitEndpointDto();
        this.dropUnitEndpointDto.setUrl(uri);
        this.dropUnitEndpointDto.setMethod(method);
        return this;
    }

    public ClientDropUnit withRequestBody(String contentType, String body)
            throws CannotProceedException {
        if (this.dropUnitEndpointDto == null) {
            throw new CannotProceedException("withEndpoint is not called before");
        }
        this.requestBodyInfo = new DropUnitRequestDto();
        this.requestBodyInfo.setRequestContentType(contentType);
        this.requestBodyInfo.setRequestBody(body);
        return this;
    }

    public ClientDropUnit withRequestBodyFromFile(String contentType, String filename)
            throws CannotProceedException, IOException {
        return withRequestBody(contentType, readFromFile(filename));
    }

    public ClientDropUnit withResponse(Response.Status status, String contentType, String body)
            throws CannotProceedException {
        if (this.dropUnitEndpointDto == null) {
            throw new CannotProceedException("withEndpoint is not called before");
        }
        this.responseDto = new DropUnitResponseDto();
        this.responseDto.setResponseCode(status.getStatusCode());
        this.responseDto.setResponseContentType(contentType);
        this.responseDto.setResponseBody(body);
        return this;
    }

    public ClientDropUnit withResponseOk(String contentType, String body)
            throws Exception {
        return withResponse(Response.Status.OK, contentType, body);
    }

    public ClientDropUnit withResponseOkFromFile(String contentType, String filename)
            throws Exception {
        return withResponseOk(contentType, readFromFile(filename));
    }

    public ClientDropUnit withResponseBadRequest(String contentType, String body)
            throws Exception {
        return withResponse(Response.Status.BAD_REQUEST, contentType, body);
    }

    public ClientDropUnit withResponseBadRequestFromFile(String contentType, String filename)
            throws Exception {
        return withResponseBadRequest(contentType, readFromFile(filename));
    }

    public ClientDropUnit withResponseBadGateway(String contentType, String body)
            throws Exception {
        return withResponse(Response.Status.GATEWAY_TIMEOUT, contentType, body);
    }

    public ClientDropUnit withResponseBadGatewayFromFile(String contentType, String filename)
            throws Exception {
        return withResponseBadGateway(contentType, readFromFile(filename));
    }

    public ClientDropUnit withRequestPattern(String contentType, String pattern)
            throws CannotProceedException {
        ArrayList<String> list = new ArrayList<>();
        list.add(pattern);
        return withRequestPatterns(contentType, list);
    }

    public ClientDropUnit withRequestPatterns(String contentType, List<String> patterns)
            throws CannotProceedException {
        if (this.dropUnitEndpointDto == null) {
            throw new CannotProceedException("withEndpoint is not called before");
        }
        this.requestPatterns = new DropUnitRequestPatternsDto();
        this.requestPatterns.setRequestContentType(contentType);
        this.requestPatterns.setPatterns(patterns);
        return this;
    }

    public ClientDropUnit withResponseDelay(int responseDelay)
            throws Exception {
        if (this.dropUnitEndpointDto == null) {
            throw new Exception("");
        }
        this.dropUnitEndpointDto.setResponseDelay(responseDelay);
        return this;
    }

    // Remote operations

    public ClientDropUnit drop()
            throws IOException {
        id = executeEndpointDelivery(dropUnitEndpointDto);
        if (requestBodyInfo != null) {
            executeRequestDelivery(id, requestBodyInfo);
        }
        if (this.requestPatterns != null) {
            executeRequestDelivery(id, requestPatterns);
        }
        executeResponseDelivery(id, responseDto);
        count = executeRetrieveCount(id);
        return this;
    }

    public void assertCountRecievedRequests(int count) {
        try {
            if (count != executeRetrieveCount(id)) {
                throw new AssertionError("incorrect request count for dropunit");
            }
        } catch (IOException e) {
            throw new AssertionError("IO failure");
        }
    }

    public void assertReceived(int number)
            throws IOException {
        if ((requestBodyInfo == null) || (requestBodyInfo.getRequestBody() == null)) {
            assertReceived(number, "");
        } else {
            assertReceived(number, requestBodyInfo.getRequestBody());
        }
    }

    public void assertReceivedFromPatterns(int number) {
        try {
            String requestBody = executeRetrieveReceived(id, number);
            for (String pattern : requestPatterns.getPatterns()) {
                assertTrue("", requestBody.contains(pattern));
            }
        } catch (IOException e) {
            throw new AssertionError("IO failure");
        }
    }

    public void assertReceivedFromFile(int number, String filename)
            throws IOException {
        assertReceived(number, readFromFile(filename));
    }

    public void assertReceived(int number, String toMatch) {
        try {
            assertThat(executeRetrieveReceived(id, number), is(toMatch));
        } catch (IOException e) {
            throw new AssertionError("IO failure");
        }
    }

    // toString
    @Override
    public String toString() {
        return "ClientDropUnit =>\n" +
                " id         = '" + id + "'\n" +
                " url        = '" + dropUnitEndpointDto.getUrl() + "'\n" +
                " count      = " + count + "\n";
    }
}