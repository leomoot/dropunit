package net.lisanza.dropunit.integrationtest.errors;

import net.lisanza.dropunit.client.ClientDropUnit;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class GetTestIT extends BaseRequest {

    @Test
    public void shouldTestWithNotFound() throws Exception {
        // setup dropunit endpoint
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST).cleanup()
                .withGet("test-get/with/path")
                .withResponseOk(MediaType.APPLICATION_XML, "")
                .drop();

        // invoke message on engine-under-test to use dropunit endpoint
        HttpResponse response = httpClient.invokeHttpGet("test-get/with/path/that/is/not/found");

        // assert message from engine-under-test
        assertEquals(404, response.getStatusLine().getStatusCode());
        dropUnit.assertCountRecievedRequests(0);
        dropUnit.assertNotFound(1);
    }
}