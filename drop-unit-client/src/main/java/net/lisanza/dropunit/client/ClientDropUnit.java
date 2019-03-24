package net.lisanza.dropunit.client;

import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.impl.rest.DropUnitRequestDto;
import net.lisanza.dropunit.impl.rest.DropUnitRequestPatternsDto;
import net.lisanza.dropunit.impl.rest.DropUnitResponseDto;

import javax.naming.CannotProceedException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientDropUnit extends BaseDropUnitClient {

    private String id;

    private int count;

    private DropUnitDto dropUnitDto = null;

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

    public DropUnitDto getDropUnitDto() {
        return dropUnitDto;
    }

    public void setDropUnitDto(DropUnitDto dropUnitDto) {
        this.dropUnitDto = dropUnitDto;
    }

    public ClientDropUnit withDropUnitDto(DropUnitDto dropUnitDto) {
        this.dropUnitDto = dropUnitDto;
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

    public String getUrl() {
        return getDropUnitDto().getUrl();
    }

    public String getResponseBody() {
        return getDropUnitDto().getResponseBodyInfo().getResponseBody();
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
        this.dropUnitDto = new DropUnitDto();
        this.dropUnitDto.setUrl(uri);
        this.dropUnitDto.setMethod(method);
        return this;
    }

    public ClientDropUnit withRequestBody(String contentType, String body)
            throws CannotProceedException {
        if (this.dropUnitDto == null) {
            throw new CannotProceedException("withEndpoint is not called before");
        }
        DropUnitRequestDto dropUnitRequestDto = new DropUnitRequestDto();
        dropUnitRequestDto.setRequestBody(contentType);
        dropUnitRequestDto.setRequestBody(body);
        this.dropUnitDto.setRequestBodyInfo(dropUnitRequestDto);
        return this;
    }

    public ClientDropUnit withRequestBodyFromFile(String contentType, String filename)
            throws CannotProceedException, IOException {
        return withRequestBody(contentType, readFromFile(filename));
    }

    public ClientDropUnit withResponse(Response.Status status, String contentType, String body)
            throws CannotProceedException {
        if (this.dropUnitDto == null) {
            throw new CannotProceedException("withEndpoint is not called before");
        }
        DropUnitResponseDto dropUnitResponseDto = new DropUnitResponseDto();
        dropUnitResponseDto.setResponseCode(status.getStatusCode());
        dropUnitResponseDto.setResponseContentType(contentType);
        dropUnitResponseDto.setResponseBody(body);
        this.dropUnitDto.setResponseBodyInfo(dropUnitResponseDto);
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
        if (this.dropUnitDto == null) {
            throw new CannotProceedException("withEndpoint is not called before");
        }
        DropUnitRequestPatternsDto requestDto = new DropUnitRequestPatternsDto();
        requestDto.setRequestContentType(contentType);
        requestDto.setPatterns(patterns);
        this.dropUnitDto.setRequestBodyPatterns(requestDto);
        return this;
    }

    public ClientDropUnit withResponseDelay(int responseDelay)
            throws Exception {
        if (this.dropUnitDto == null) {
            throw new Exception("");
        }
        this.dropUnitDto.setResponseDelay(responseDelay);
        return this;
    }


    // Remote operations

    public ClientDropUnit drop()
            throws IOException {
        id = executeEndpointDelivery(dropUnitDto);
        if (dropUnitDto.getResponseBodyInfo() != null) {
            executeRequestDelivery(id, dropUnitDto.getRequestBodyInfo());
        }
        if (dropUnitDto.getRequestBodyPatterns() != null) {
            executeRequestDelivery(id, dropUnitDto.getRequestBodyPatterns());
        }
        executeResponseDelivery(id, dropUnitDto);
        count = executeRetrieveCount(id);
        return this;
    }


    public void assertCount(int count) {
        try {
            if (count != executeRetrieveCount(id)) {
                throw new AssertionError("incorrect request count for dropunit");
            }
        } catch (IOException e) {
            throw new AssertionError("IO failure");
        }
    }


    // toString
    @Override
    public String toString() {
        return "ClientDropUnit =>\n" +
                " id         = '" + id + "'\n" +
                " url        = '" + dropUnitDto.getUrl() + "'\n" +
                " count      = " + count + "\n";
    }
}