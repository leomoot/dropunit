package net.lisanza.dropunit.integrationtest.httppost;

import net.lisanza.dropunit.client.ClientDropUnit;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class PostWithPathTestIT extends BaseRequest {

    private static final String REQUEST_FILE = "src/test/resources/xml/drop-request.xml";
    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithPath() throws Exception {
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withPost("test-post/with/path")
                .withRequestBodyFromFile(MediaType.APPLICATION_XML, REQUEST_FILE)
                .withResponseOkFromFile(MediaType.APPLICATION_XML, RESPONSE_FILE)
                .drop();

        HttpResponse response = httpClient.executeBasicHttpPost(dropUnit.getUrl(), REQUEST_FILE, MediaType.APPLICATION_XML);
        assertEquals(200, response.getStatusLine().getStatusCode());

        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        assertNotNull(body);
        assertThat(body, containsString(dropUnit.getResponseBody()));

        dropUnit.assertCount(1);
    }
}
