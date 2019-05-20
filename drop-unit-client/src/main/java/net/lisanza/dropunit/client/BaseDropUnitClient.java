package net.lisanza.dropunit.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lisanza.dropunit.impl.rest.dto.DropUnitEndpointDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitRequestDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitRequestPatternsDto;
import net.lisanza.dropunit.impl.rest.dto.DropUnitResponseDto;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.DROP_UNIT_SERVICE;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_CLEARALLDROPS;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_COUNT_DROPID;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_COUNT_NOTFOUND;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_DELIVERY_ENDPOINT;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_DELIVERY_ENDPOINT_DROPID;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_DELIVERY_ENDPOINT_DROPID_REQUESTBODY;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_DELIVERY_ENDPOINT_DROPID_RESPONSEBODY_STATUS;
import static net.lisanza.dropunit.impl.rest.constants.RequestMappings.URI_RECIEVED_DROPID_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class BaseDropUnitClient extends BaseHttpClient {

    public BaseDropUnitClient(String baseUrl) {
        super(baseUrl + DROP_UNIT_SERVICE);
    }

    private static final String DROP_DELIVERY = "drop-delivery";
    private static final String REQUEST_DELIVERY = "request-delivery";
    private static final String RESPONSE_DELIVERY = "response-delivery";
    private static final String DROP_DELETION = "drop-deletion";

    public String executeEndpointDelivery(DropUnitEndpointDto dropUnit) {
        try {
            HttpResponse response = invokeHttpPost(URI_DELIVERY_ENDPOINT, dropUnit);
            assertOkStatus(DROP_DELIVERY, response.getStatusLine());
            HttpEntity entity = response.getEntity();
            JsonNode obj = new ObjectMapper().readTree(entity.getContent());
            assertResult(DROP_DELIVERY, obj);
            JsonNode idValue = obj.get("id");
            assertNotNull("no id in response-body for drop-delivery", idValue);
            EntityUtils.consume(entity);
            return idValue.asText();
        } catch (IOException e) {
            fail("IO failure");
        }
        return null; // will never happen
    }

    public void executeRequestDelivery(String id, DropUnitRequestDto requestDto) {
        try {
            if (requestDto != null) {
                HttpResponse response = invokeHttpPut(URI_DELIVERY_ENDPOINT_DROPID_REQUESTBODY
                                .replace("{dropId}", id),
                        requestDto.getRequestContentType(),
                        requestDto.getRequestBody());
                assertOkStatus(REQUEST_DELIVERY, response.getStatusLine());
                HttpEntity entity = response.getEntity();
                JsonNode obj = new ObjectMapper().readTree(entity.getContent());
                assertResult(REQUEST_DELIVERY, obj);
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            fail("IO failure");
        }
    }

    public void executeRequestDelivery(String id, DropUnitRequestPatternsDto requestDto) {
        try {
            if (requestDto != null) {
                HttpResponse response = invokeHttpPost(URI_DELIVERY_ENDPOINT_DROPID_REQUESTBODY
                                .replace("{dropId}", id),
                        requestDto);
                assertOkStatus(REQUEST_DELIVERY, response.getStatusLine());
                HttpEntity entity = response.getEntity();
                JsonNode obj = new ObjectMapper().readTree(entity.getContent());
                assertResult(REQUEST_DELIVERY, obj);
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            fail("IO failure");
        }
    }

    public void executeResponseDelivery(String id, DropUnitResponseDto responseDto) {
        try {
            if (responseDto != null) {
                HttpResponse response = invokeHttpPut(URI_DELIVERY_ENDPOINT_DROPID_RESPONSEBODY_STATUS
                                .replace("{dropId}", id)
                                .replace("{status}", responseDto.getResponseCode() + ""),
                        responseDto.getResponseContentType(),
                        responseDto.getResponseBody());
                assertOkStatus(RESPONSE_DELIVERY, response.getStatusLine());
                HttpEntity entity = response.getEntity();
                JsonNode obj = new ObjectMapper().readTree(entity.getContent());
                assertResult(RESPONSE_DELIVERY, obj);
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            fail("IO fasilure");
        }
    }

    public int executeRetrieveCount(String dropId) {
        try {
            HttpResponse response = invokeHttpGet(URI_COUNT_DROPID
                    .replace("{dropId}", dropId));
            assertEquals("incorrect response code", 200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            JsonNode obj = new ObjectMapper().readTree(entity.getContent());
            assertNotNull("no response-body for drop-delivery", obj);
            JsonNode countValue = obj.get("count");
            assertNotNull("no count in response-body for drop-delivery", countValue);
            EntityUtils.consume(entity);
            return countValue.asInt();
        } catch (IOException e) {
            fail("IO fasilure");
        }
        return -1; // will never happen
    }

    public String executeRetrieveReceived(String dropId, int number) {
        try {
            HttpResponse response = invokeHttpGet(URI_RECIEVED_DROPID_NUMBER
                    .replace("{dropId}", dropId)
                    .replace("{number}", number + ""));
            assertEquals("incorrect response code", 200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            String responsebody = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            return responsebody;
        } catch (IOException e) {
            fail("IO fasilure");
        }
        return null; // will never happen
    }

    public int executeRetrieveNotFound() {
        try {
            HttpResponse response = invokeHttpGet(URI_COUNT_NOTFOUND);
            assertOkStatus("retrieve not found", response.getStatusLine());
            HttpEntity entity = response.getEntity();
            JsonNode obj = new ObjectMapper().readTree(entity.getContent());
            assertNotNull("no response-body for drop-delivery", obj);
            JsonNode countValue = obj.get("count");
            assertNotNull("no count in response-body for drop-delivery", countValue);
            EntityUtils.consume(entity);
            return countValue.asInt();
        } catch (IOException e) {
            fail("IO fasilure");
        }
        return -1; // will never happen
    }

    public void executeEndpointDeletion(String id) {
        try {
            HttpResponse response = invokeHttpDelete(URI_DELIVERY_ENDPOINT_DROPID
                    .replace("{dropId}", id));
            assertOkStatus(DROP_DELETION, response.getStatusLine());
            HttpEntity entity = response.getEntity();
            JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
            assertResult(DROP_DELETION, obj);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            fail("IO fasilure");
        }
    }

    public void executeEndpointDeletion()
            throws IOException {
        HttpResponse response = invokeHttpDelete(URI_CLEARALLDROPS);
        assertOkStatus(DROP_DELETION, response.getStatusLine());
        EntityUtils.consume(response.getEntity());
    }

    // private HTTP operations

    private HttpResponse invokeHttpPost(String deliveryEndpoint, Object dropUnit)
            throws IOException {
        String bodyRequest = new ObjectMapper().writeValueAsString(dropUnit);
        return super.invokeHttpPost(deliveryEndpoint, MediaType.APPLICATION_JSON, bodyRequest);
    }

    // asserts

    private void assertResult(String message, JsonNode obj) {
        assertNotNull("no response-body for " + message, obj);
        JsonNode resultValue = obj.get("result");
        assertNotNull("no result in response-body for " + message, resultValue);
        assertEquals("failure for " + message, "OK", resultValue.textValue());
    }

    private void assertOkStatus(String message, StatusLine status) {
        assertEquals("incorrect response code in " + message, 200, status.getStatusCode());
    }
}