package net.lisanza.dropunit.integrationtest.httppost;

import net.lisanza.dropunit.client.ClientDropUnit;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class PostWithExceptionTestIT extends BaseRequest {

    private static final String REQUEST_FILE = "src/test/resources/xml/drop-request.xml";
    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithException() throws Exception {
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withPost("test-post-exception")
                .withRequestBodyFromFile(MediaType.APPLICATION_XML, REQUEST_FILE)
                .withResponseBadRequest(MediaType.APPLICATION_XML, "")
                .drop();

        HttpResponse response = httpClient.executeBasicHttpPost(dropUnit.getUrl(), REQUEST_FILE, MediaType.APPLICATION_XML);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

        dropUnit.assertCount(1);
    }
}