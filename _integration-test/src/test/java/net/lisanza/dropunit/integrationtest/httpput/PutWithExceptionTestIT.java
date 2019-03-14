package net.lisanza.dropunit.integrationtest.httpput;

import net.lisanza.dropunit.client.DropFactory;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PutWithExceptionTestIT extends BaseRequest {

    private static final String REQUEST_FILE = "src/test/resources/xml/drop-request.xml";

    private DropUnitDto dropUnit;
    private int count;

    @Before
    public void setUp() throws Exception {
        dropUnit = DropFactory.createDropUnit("test-put-exception",
                "PUT", MediaType.APPLICATION_XML, REQUEST_FILE,
                Response.Status.BAD_REQUEST, MediaType.APPLICATION_XML, null);

        dropUnitClient.executeDropDelivery(dropUnit);

        count = dropUnitClient.executeRetrieveCount("put");
    }

    @Test
    public void shouldTestWithException() throws Exception {
        HttpResponse response = httpClient.executeBasicHttpPut(dropUnit.getUrl(), REQUEST_FILE, MediaType.APPLICATION_XML);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

        assertThat(count + 1, is(dropUnitClient.executeRetrieveCount("put")));
    }
}
