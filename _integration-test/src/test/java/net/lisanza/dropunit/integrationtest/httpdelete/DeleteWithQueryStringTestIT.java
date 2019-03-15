package net.lisanza.dropunit.integrationtest.httpdelete;

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

public class DeleteWithQueryStringTestIT extends BaseRequest {

    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithQueryString() throws Exception {
        ClientDropUnitDto dropUnit = dropUnitClient.drop("test-delete", "DELETE",
                Response.Status.OK, MediaType.APPLICATION_XML, RESPONSE_FILE);

        HttpResponse response = httpClient.executeBasicHttpDelete(dropUnit.getUrl());
        assertEquals(200, response.getStatusLine().getStatusCode());

        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        assertNotNull(body);
        assertThat(body, containsString(dropUnit.getResponseBody()));

        assertThat(dropUnit.getCount() + 1, is(dropUnitClient.executeRetrieveCount(dropUnit)));
    }
}