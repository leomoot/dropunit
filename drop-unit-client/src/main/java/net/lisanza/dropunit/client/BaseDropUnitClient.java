package net.lisanza.dropunit.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.impl.rest.DropUnitRequestDto;
import net.lisanza.dropunit.impl.rest.DropUnitRequestPatternsDto;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class BaseDropUnitClient extends BaseHttpClient {

    public BaseDropUnitClient(String baseUrl) {
        super(baseUrl);
    }

    private static final String DROPUNIT_DELIVERY_ENDPOINT = "dropunit/delivery/endpoint";
    private static final String DELIVERY_ENDPOINT_REQUEST_BODY = "dropunit/delivery/endpoint/{dropId}/request-body";
    private static final String DELIVERY_ENDPOINT_RESPONSE_BODY = "dropunit/delivery/endpoint/{dropId}/response-body/{status}";
    private static final String URI_DROP_COUNT = "dropunit/getDropCount/";

    public String executeEndpointDelivery(DropUnitDto dropUnit)
            throws IOException {
        HttpResponse response = invokeHttpPost(baseUrl + DROPUNIT_DELIVERY_ENDPOINT, dropUnit);
        assertStatus("drop-delivery", response.getStatusLine());
        JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
        assertResult("drop-delivery", obj);
        JsonNode idValue = obj.get("id");
        if (idValue == null) {
            throw new AssertionError("no id in response-body for drop-delivery");
        }
        return idValue.asText();
    }

    private HttpResponse invokeHttpPost(String deliveryEndpoint, Object dropUnit)
            throws IOException {
        HttpClient client = getHttpClient(null);
        HttpPost httpPost = new HttpPost(deliveryEndpoint);

        String bodyRequest = new ObjectMapper().writeValueAsString(dropUnit);

        StringEntity entity = new StringEntity(bodyRequest, "UTF-8");
        entity.setContentType(MediaType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        return client.execute(httpPost);
    }

    public void executeRequestDelivery(String id, DropUnitRequestDto requestDto)
            throws IOException {
        if (requestDto != null) {
            HttpResponse response = invokeHttpPut(baseUrl + DELIVERY_ENDPOINT_REQUEST_BODY
                            .replace("{dropId}", id),
                    requestDto.getRequestContentType(),
                    requestDto.getRequestBody());
            assertStatus("request-delivery", response.getStatusLine());
            JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
            assertResult("request-delivery", obj);
        }
    }

    public void executeRequestDelivery(String id, DropUnitRequestPatternsDto requestDto)
            throws IOException {
        if (requestDto != null) {
            HttpResponse response = invokeHttpPost(baseUrl + DELIVERY_ENDPOINT_REQUEST_BODY
                    .replace("{dropId}", id),
                    requestDto);
            assertStatus("request-delivery", response.getStatusLine());
            JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
            assertResult("request-delivery", obj);
        }
    }

    public void executeResponseDelivery(String id, DropUnitDto dropUnit)
            throws IOException {
        if (dropUnit.getResponseBodyInfo() != null) {
            HttpResponse response = invokeHttpPut(baseUrl + DELIVERY_ENDPOINT_RESPONSE_BODY
                            .replace("{dropId}", id)
                            .replace("{status}", dropUnit.getResponseBodyInfo().getResponseCode() + ""),
                    dropUnit.getResponseBodyInfo().getResponseContentType(),
                    dropUnit.getResponseBodyInfo().getResponseBody());
            assertStatus("response-delivery", response.getStatusLine());
            JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
            assertResult("response-delivery", obj);
        }
    }

    private HttpResponse invokeHttpPut(String deliveryEndpoint, String contentType, String bodyRequest)
            throws IOException {
        HttpClient client = getHttpClient(null);
        HttpPut httpPut = new HttpPut(deliveryEndpoint);

        StringEntity entity = new StringEntity(bodyRequest, "UTF-8");
        entity.setContentType(contentType);
        httpPut.setEntity(entity);

        return client.execute(httpPut);
    }

    public int executeRetrieveCount(String dropUnitId) throws IOException {
        HttpResponse response = executeBasicHttpGet(URI_DROP_COUNT + dropUnitId);
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