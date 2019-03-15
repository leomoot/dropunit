package net.lisanza.dropunit.integrationtest.httpput;

import net.lisanza.dropunit.client.ClientDropUnitDto;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PutWithExceptionTestIT extends BaseRequest {

    private static final String REQUEST_FILE = "src/test/resources/xml/drop-request.xml";

    @Test
    public void shouldTestWithException() throws Exception {
        ClientDropUnitDto dropUnit = dropUnitClient.drop("test-put-exception",
                "PUT", MediaType.APPLICATION_XML, REQUEST_FILE,
                Response.Status.BAD_REQUEST, MediaType.APPLICATION_XML, null);

        HttpResponse response = httpClient.executeBasicHttpPut(dropUnit.getUrl(), REQUEST_FILE, MediaType.APPLICATION_XML);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

        assertThat(dropUnit.getCount() + 1, is(dropUnitClient.executeRetrieveCount(dropUnit)));
    }
}
