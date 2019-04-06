package net.lisanza.dropunit.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lisanza.dropunit.impl.rest.dto.DropUnitEndpointDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitRequestDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitRequestPatternsDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitResponseDto;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.DROP_UNIT_SERVICE;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_COUNT_DROPID;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_DELIVERY_ENDPOINT;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_DELIVERY_ENDPOINT_DROPID;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_DELIVERY_ENDPOINT_DROPID_REQUESTBODY;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_DELIVERY_ENDPOINT_DROPID_RESPONSEBODY_STATUS;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_RECIEVED_DROPID_NUMBER;

public class BaseDropUnitClient extends BaseHttpClient {

    public BaseDropUnitClient(String baseUrl) {
        super(baseUrl + DROP_UNIT_SERVICE);
    }

    private static final String DROP_DELIVERY = "drop-delivery";
    private static final String REQUEST_DELIVERY = "request-delivery";
    private static final String RESPONSE_DELIVERY = "response-delivery";
    private static final String DROP_DELETION = "drop-deletion";

    public String executeEndpointDelivery(DropUnitEndpointDto dropUnit)
            throws IOException {
        HttpResponse response = invokeHttpPost(URI_DELIVERY_ENDPOINT, dropUnit);
        assertStatus(DROP_DELIVERY, response.getStatusLine());
        JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
        assertResult(DROP_DELIVERY, obj);
        JsonNode idValue = obj.get("id");
        if (idValue == null) {
            throw new AssertionError("no id in response-body for drop-delivery");
        }
        return idValue.asText();
    }

    public void executeRequestDelivery(String id, DropUnitRequestDto requestDto)
            throws IOException {
        if (requestDto != null) {
            HttpResponse response = invokeHttpPut(URI_DELIVERY_ENDPOINT_DROPID_REQUESTBODY
                            .replace("{dropId}", id),
                    requestDto.getRequestContentType(),
                    requestDto.getRequestBody());
            assertStatus(REQUEST_DELIVERY, response.getStatusLine());
            JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
            assertResult(REQUEST_DELIVERY, obj);
        }
    }

    public void executeRequestDelivery(String id, DropUnitRequestPatternsDto requestDto)
            throws IOException {
        if (requestDto != null) {
            HttpResponse response = invokeHttpPost(URI_DELIVERY_ENDPOINT_DROPID_REQUESTBODY
                            .replace("{dropId}", id),
                    requestDto);
            assertStatus(REQUEST_DELIVERY, response.getStatusLine());
            JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
            assertResult(REQUEST_DELIVERY, obj);
        }
    }

    public void executeResponseDelivery(String id, DropUnitResponseDto responseDto)
            throws IOException {
        if (responseDto != null) {
            HttpResponse response = invokeHttpPut(URI_DELIVERY_ENDPOINT_DROPID_RESPONSEBODY_STATUS
                            .replace("{dropId}", id)
                            .replace("{status}", responseDto.getResponseCode() + ""),
                    responseDto.getResponseContentType(),
                    responseDto.getResponseBody());
            assertStatus(RESPONSE_DELIVERY, response.getStatusLine());
            JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
            assertResult(RESPONSE_DELIVERY, obj);
        }
    }

    public int executeRetrieveCount(String dropId) throws IOException {
        HttpResponse response = invokeHttpGet(URI_COUNT_DROPID
                .replace("{dropId}", dropId));
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new AssertionError("incorrect response code");
        }
        JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
        if (obj == null) {
            throw new AssertionError("no response-body for drop-delivery");
        }
        JsonNode countValue = obj.get("count");
        if (countValue == null) {
            throw new AssertionError("no count in response-body for drop-delivery");
        }
        return countValue.asInt();
    }

    public String executeRetrieveReceived(String dropId, int number) throws IOException {
        HttpResponse response = invokeHttpGet(URI_RECIEVED_DROPID_NUMBER
                .replace("{dropId}", dropId)
                .replace("{number}", number + ""));
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new AssertionError("incorrect response code");
        }
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    public void executeEndpointDeletion(String id)
            throws IOException {
        HttpResponse response = invokeHttpDelete(URI_DELIVERY_ENDPOINT_DROPID
                .replace("{dropId}", id));
        assertStatus(DROP_DELETION, response.getStatusLine());
        JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
        assertResult(DROP_DELETION, obj);
    }

    // private HTTP operations

    private HttpResponse invokeHttpPost(String deliveryEndpoint, Object dropUnit)
            throws IOException {
        String bodyRequest = new ObjectMapper().writeValueAsString(dropUnit);
        return super.invokeHttpPost(deliveryEndpoint, MediaType.APPLICATION_JSON, bodyRequest);
    }

    // asserts

    private void assertResult(String message, JsonNode obj) {
        if (obj == null) {
            throw new AssertionError("no response-body for " + message);
        }
        JsonNode resultValue = obj.get("result");
        if (resultValue == null) {
            throw new AssertionError("no result in response-body for " + message);
        }
        if (!resultValue.asText().equals("OK")) {
            throw new AssertionError("failure for " + message);
        }
    }

    private void assertStatus(String message, StatusLine status) {
        if (200 != status.getStatusCode()) {
            throw new AssertionError("incorrect response code in " + message);
        }
    }
}