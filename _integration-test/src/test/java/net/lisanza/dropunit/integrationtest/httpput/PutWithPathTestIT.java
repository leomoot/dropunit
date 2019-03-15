package net.lisanza.dropunit.integrationtest.httpput;

import net.lisanza.dropunit.client.ClientDropUnitDto;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class PutWithPathTestIT extends BaseRequest {

    private static final String REQUEST_FILE = "src/test/resources/xml/drop-request.xml";
    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithPath() throws Exception {
        ClientDropUnitDto dropUnit = dropUnitClient.drop("test-put/with/path",
                "PUT", MediaType.APPLICATION_XML, REQUEST_FILE,
                Response.Status.OK, MediaType.APPLICATION_XML, RESPONSE_FILE);

        HttpResponse response = httpClient.executeBasicHttpPut(dropUnit.getUrl(), REQUEST_FILE, MediaType.APPLICATION_XML);
        assertEquals(200, response.getStatusLine().getStatusCode());

        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        assertNotNull(body);
        assertThat(body, containsString(dropUnit.getResponseBody()));

        assertThat(dropUnit.getCount() + 1, is(dropUnitClient.executeRetrieveCount(dropUnit)));
    }
}
