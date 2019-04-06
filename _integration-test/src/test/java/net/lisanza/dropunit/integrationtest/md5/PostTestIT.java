package net.lisanza.dropunit.integrationtest.md5;

import net.lisanza.dropunit.client.ClientDropUnit;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.SocketTimeoutException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class PostTestIT extends BaseRequest {

    private static final String REQUEST_FILE = "src/test/resources/xml/drop-request.xml";
    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithPath() throws Exception {

        // setup dropunit endpoint
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withPost("test-post/with/path")
                .withRequestBodyFromFile(MediaType.APPLICATION_XML, REQUEST_FILE)
                .withResponseOkFromFile(MediaType.APPLICATION_XML, RESPONSE_FILE)
                .drop();

        // invoke message on engine-under-test to use dropunit endpoint
        HttpResponse response = httpClient.invokeHttpPost(dropUnit.getUrl(),
                MediaType.APPLICATION_XML, new File(REQUEST_FILE));

        // assert message from engine-under-test
        assertEquals(200, response.getStatusLine().getStatusCode());
        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        assertNotNull(body);
        assertThat(body, containsString(dropUnit.getResponseBody()));

        dropUnit.assertCountRecievedRequests(1);
        dropUnit.assertReceivedFromFile(1, REQUEST_FILE);
    }

    @Test
    public void shouldTestWithQueryString() throws Exception {

        // setup dropunit endpoint
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withPost("test-post/with/path?and=variables")
                .withRequestBodyFromFile(MediaType.APPLICATION_XML, REQUEST_FILE)
                .withResponseOkFromFile(MediaType.APPLICATION_XML, RESPONSE_FILE)
                .drop();

        // invoke message on engine-under-test to use dropunit endpoint
        HttpResponse response = httpClient.invokeHttpPost(dropUnit.getUrl(), MediaType.APPLICATION_XML, new File(REQUEST_FILE));

        // assert message from engine-under-test
        assertEquals(200, response.getStatusLine().getStatusCode());
        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        assertNotNull(body);
        assertThat(body, containsString(dropUnit.getResponseBody()));

        dropUnit.assertCountRecievedRequests(1);
        dropUnit.assertReceivedFromFile(1, REQUEST_FILE);
    }

    @Test
    public void shouldTestWithException() throws Exception {

        // setup dropunit endpoint
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withPost("test-post-exception")
                .withRequestBodyFromFile(MediaType.APPLICATION_XML, REQUEST_FILE)
                .withResponseBadRequest(MediaType.APPLICATION_XML, "")
                .drop();

        // invoke message on engine-under-test to use dropunit endpoint
        HttpResponse response = httpClient.invokeHttpPost(dropUnit.getUrl(),
                MediaType.APPLICATION_XML, new File(REQUEST_FILE));

        // assert message from engine-under-test
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

        dropUnit.assertCountRecievedRequests(1);
        dropUnit.assertReceivedFromFile(1, REQUEST_FILE);
    }

    @Test
    public void shouldTestWithConnectionTimeout() throws Exception {

        // setup dropunit endpoint
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withPost("test-post")
                .withRequestBodyFromFile(MediaType.APPLICATION_XML, REQUEST_FILE)
                .withResponseOkFromFile(MediaType.APPLICATION_XML, RESPONSE_FILE)
                .withResponseDelay(20000)
                .drop();

        // invoke message on engine-under-test to use dropunit endpoint
        // and assert message from engine-under-test
        try {
            httpClient.invokeHttpPost(dropUnit.getUrl(),
                    MediaType.APPLICATION_XML, new File(REQUEST_FILE),
                    RequestConfig.custom()
                            .setConnectionRequestTimeout(1000)
                            .setConnectTimeout(1000)
                            .setSocketTimeout(1000)
                            .build());
            fail("timeout not exceeded");
        } catch (SocketTimeoutException e) {
            assertTrue(true);
        }

        dropUnit.assertCountRecievedRequests(1);
        dropUnit.assertReceivedFromFile(1, REQUEST_FILE);
    }
}