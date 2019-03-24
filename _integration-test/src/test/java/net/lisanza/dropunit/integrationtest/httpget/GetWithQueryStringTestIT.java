package net.lisanza.dropunit.integrationtest.httpget;

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

public class GetWithQueryStringTestIT extends BaseRequest {

    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithQueryString() throws Exception {
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withGet("test-get/with/path?and=variables")
                .withResponseOkFromFile(MediaType.APPLICATION_XML, RESPONSE_FILE)
                .drop();

        HttpResponse response = httpClient.executeBasicHttpGet(dropUnit.getUrl());
        assertEquals(200, response.getStatusLine().getStatusCode());

        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        assertNotNull(body);
        assertThat(body, containsString(dropUnit.getResponseBody()));

        dropUnit.assertCount(1);
    }
}
