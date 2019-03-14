package net.lisanza.dropunit.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class DropUnitClient extends BaseHttpClient {

    public DropUnitClient(String baseUrl) {
        super(baseUrl);
    }

    private static final String URI_DROP_DELIVERY = "dropunit/delivery/";
    private static final String URI_DROP_COUNT = "dropunit/getDropCount";

    private static int i = 0;

    public void executeDropDelivery(DropUnitDto dropUnit)
            throws IOException {
        HttpResponse delivery = executeDropDelivery(baseUrl + URI_DROP_DELIVERY + ++i, dropUnit);
        if (200 != delivery.getStatusLine().getStatusCode()) {
            throw new AssertionError("incorrect response code in drop-delivery");
        }
        String deliveryBody = EntityUtils.toString(delivery.getEntity(), "UTF-8");
        if (null == deliveryBody) {
            throw new AssertionError("no response-body in drop-delivery");
        }
        if (!deliveryBody.contains("droppy registered")) {
            throw new AssertionError("incorrect response expected in drop-delivery");
        }
    }

    private HttpResponse executeDropDelivery(String deliveryEndpoint, DropUnitDto dropUnit)
            throws IOException {
        HttpClient client = getHttpClient(null);
        HttpPost httpPost = new HttpPost(deliveryEndpoint);

        String bodyRequest = new ObjectMapper().writeValueAsString(dropUnit);

        StringEntity entity = new StringEntity(bodyRequest, "UTF-8");
        entity.setContentType(MediaType.APPLICATION_JSON);
        httpPost.setEntity(entity);

        return client.execute(httpPost);
    }

    public int executeRetrieveCount(String name) throws IOException {
        HttpResponse response = executeBasicHttpGet(URI_DROP_COUNT);
        if (response.getStatusLine().getStatusCode() != 200) {
            return -1;
        }
        JsonNode obj = new ObjectMapper().readTree(response.getEntity().getContent());
        if (obj == null) {
            return -2;
        }
        JsonNode value = obj.get(name);
        if (value == null) {
            return -3;
        }
        return value.asInt();
    }

    public String readFromFile(String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(new File(fileName))) {
            String result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            return result;
        }
    }
}