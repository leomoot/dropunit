package net.lisanza.dropunit.integrationtest.httpget;

import net.lisanza.dropunit.client.ClientDropUnitDto;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GetWithExceptionTestIT extends BaseRequest {

    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithException() throws Exception {
        ClientDropUnitDto dropUnit = dropUnitClient.drop("test-get-exception", "GET",
                Response.Status.BAD_REQUEST, MediaType.APPLICATION_XML, null);

        HttpResponse response = httpClient.executeBasicHttpGet(dropUnit.getUrl());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

        assertThat(dropUnit.getCount() + 1, is(dropUnitClient.executeRetrieveCount(dropUnit)));
    }

}
