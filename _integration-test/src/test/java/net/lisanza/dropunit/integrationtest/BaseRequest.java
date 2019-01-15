package net.lisanza.dropunit.integrationtest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class BaseRequest {

    protected static final String ENDPOINT_HOST = "http://127.0.0.1:9080/";
    protected static final String DROP_DELIVERY = ENDPOINT_HOST + "dropunit/delivery/";
    protected static final String DROP_COUNT = ENDPOINT_HOST + "dropunit/getDropCount";

    protected static final ContentType XML = ContentType.XML;
    protected static final ContentType JSON = ContentType.JSON;

    protected static int i = 0;

    protected HttpResponse executeDropDelivery(DropUnitDto dropUnit)
            throws IOException {
        return executeDropDelivery(DROP_DELIVERY + ++i, dropUnit);
    }

    protected HttpResponse executeDropDelivery(String deliveryEndpoint, DropUnitDto dropUnit)
            throws IOException {
        HttpClient client = getHttpClient(null);
        HttpPost httpPost = new HttpPost(deliveryEndpoint);

        String bodyRequest = new ObjectMapper().writeValueAsString(dropUnit);

        StringEntity entity = new StringEntity(bodyRequest, "UTF-8");
        entity.setContentType(JSON.getContentType());
        httpPost.setEntity(entity);

        return client.execute(httpPost);
    }

    protected HttpResponse executeBasicHttpPost(String endpoint,
                                                String requestData,
                                                ContentType contentType)
            throws IOException {
        return executeBasicHttpPost(endpoint, requestData, contentType, null);
    }

    protected HttpResponse executeBasicHttpPost(String endpoint,
                                                String requestData,
                                                ContentType contentType,
                                                RequestConfig requestConfig)
            throws IOException {
        HttpClient client = getHttpClient(requestConfig);
        HttpPost httpPost = new HttpPost(endpoint);

        String bodyRequest = "";
        if (requestData != null) {
            bodyRequest = readFromFile(requestData);
        }

        StringEntity entity = new StringEntity(bodyRequest, "UTF-8");
        entity.setContentType(contentType.getContentType());
        httpPost.setEntity(entity);

        return client.execute(httpPost);
    }

    protected HttpResponse executeBasicHttpPut(String endpoint,
                                               String requestData,
                                               ContentType contentType)
            throws IOException {
        return executeBasicHttpPut(endpoint, requestData, contentType, null);
    }

    protected HttpResponse executeBasicHttpPut(String endpoint,
                                               String requestData,
                                               ContentType contentType,
                                               RequestConfig requestConfig)
            throws IOException {
        HttpClient client = getHttpClient(requestConfig);
        HttpPut httpPut = new HttpPut(endpoint);

        String bodyRequest = "";
        if (requestData != null) {
            bodyRequest = readFromFile(requestData);
        }

        StringEntity entity = new StringEntity(bodyRequest, "UTF-8");
        entity.setContentType(contentType.getContentType());
        httpPut.setEntity(entity);

        return client.execute(httpPut);
    }

    protected HttpResponse executeBasicHttpDelete(String endpoint)
            throws IOException {
        return executeBasicHttpDelete(endpoint, null);
    }

    protected HttpResponse executeBasicHttpDelete(String endpoint,
                                                  RequestConfig requestConfig)
            throws IOException {
        HttpClient client = getHttpClient(requestConfig);
        HttpDelete httpDelete = new HttpDelete(endpoint);
        return client.execute(httpDelete);
    }

    protected HttpResponse executeBasicHttpGet(String endpoint)
            throws IOException {
        return executeBasicHttpGet(endpoint, null);
    }

    protected HttpResponse executeBasicHttpGet(String endpoint,
                                               RequestConfig requestConfig) throws IOException {
        HttpClient client = getHttpClient(requestConfig);
        HttpGet request = new HttpGet(endpoint);
        return client.execute(request);
    }

    protected HttpClient getHttpClient(RequestConfig requestConfig) {
        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }

    protected int executeRetrieveCount(String name) throws IOException {
        HttpResponse response = executeBasicHttpGet(DROP_COUNT);
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

    enum ContentType {
        XML("application/xml"),
        JSON("application/json");

        private final String contentType;

        ContentType(String contentTypeStr) {
            this.contentType = contentTypeStr;
        }

        public String getContentType() {
            return contentType;
        }
    }

}