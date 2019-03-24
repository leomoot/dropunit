package net.lisanza.dropunit.integrationtest.httpget;

import net.lisanza.dropunit.client.ClientDropUnit;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class GetWithExceptionTestIT extends BaseRequest {

    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithException() throws Exception {
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withGet("test-get-exception")
                .withResponseBadRequest(MediaType.APPLICATION_XML, "")
                .drop();

        HttpResponse response = httpClient.executeBasicHttpGet(dropUnit.getUrl());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

        dropUnit.assertCount(1);
    }

}
