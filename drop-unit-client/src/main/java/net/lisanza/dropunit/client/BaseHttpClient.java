package net.lisanza.dropunit.client;

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

public class BaseHttpClient {

    protected final String baseUrl;

    public BaseHttpClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public HttpResponse executeBasicHttpPost(String endpoint,
                                             String requestData,
                                             String contentType)
            throws IOException {
        return executeBasicHttpPost(endpoint, requestData, contentType, null);
    }

    public HttpResponse executeBasicHttpPost(String endpoint,
                                             String requestData,
                                             String contentType,
                                             RequestConfig requestConfig)
            throws IOException {
        org.apache.http.client.HttpClient client = getHttpClient(requestConfig);
        HttpPost httpPost = new HttpPost(baseUrl + endpoint);

        String bodyRequest = "";
        if (requestData != null) {
            bodyRequest = readFromFile(requestData);
        }

        StringEntity entity = new StringEntity(bodyRequest, "UTF-8");
        entity.setContentType(contentType);
        httpPost.setEntity(entity);

        return client.execute(httpPost);
    }

    public HttpResponse executeBasicHttpPut(String endpoint,
                                            String requestData,
                                            String contentType)
            throws IOException {
        return executeBasicHttpPut(endpoint, requestData, contentType, null);
    }

    public HttpResponse executeBasicHttpPut(String endpoint,
                                            String requestData,
                                            String contentType,
                                            RequestConfig requestConfig)
            throws IOException {
        org.apache.http.client.HttpClient client = getHttpClient(requestConfig);
        HttpPut httpPut = new HttpPut(baseUrl + endpoint);

        String bodyRequest = "";
        if (requestData != null) {
            bodyRequest = readFromFile(requestData);
        }

        StringEntity entity = new StringEntity(bodyRequest, "UTF-8");
        entity.setContentType(contentType);
        httpPut.setEntity(entity);

        return client.execute(httpPut);
    }

    public HttpResponse executeBasicHttpDelete(String endpoint)
            throws IOException {
        return executeBasicHttpDelete(endpoint, null);
    }

    public HttpResponse executeBasicHttpDelete(String endpoint,
                                               RequestConfig requestConfig)
            throws IOException {
        org.apache.http.client.HttpClient client = getHttpClient(requestConfig);
        HttpDelete httpDelete = new HttpDelete(baseUrl + endpoint);
        return client.execute(httpDelete);
    }

    public HttpResponse executeBasicHttpGet(String endpoint)
            throws IOException {
        return executeBasicHttpGet(endpoint, null);
    }

    public HttpResponse executeBasicHttpGet(String endpoint,
                                            RequestConfig requestConfig) throws IOException {
        HttpClient client = getHttpClient(requestConfig);
        HttpGet request = new HttpGet(baseUrl + endpoint);
        return client.execute(request);
    }

    protected HttpClient getHttpClient(RequestConfig requestConfig) {
        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }

    public String readFromFile(String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(new File(fileName))) {
            String result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            return result;
        }
    }
}
