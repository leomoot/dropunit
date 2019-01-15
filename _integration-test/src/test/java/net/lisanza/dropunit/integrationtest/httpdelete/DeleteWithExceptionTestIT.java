package net.lisanza.dropunit.integrationtest.httpdelete;

import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import net.lisanza.dropunit.integrationtest.DropFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DeleteWithExceptionTestIT extends BaseRequest {

    private static final String REQUEST_FILE = "src/test/resources/xml/drop-request.xml";
    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    private DropUnitDto dropUnit;
    private int count;

    @Before
    public void setUp() throws Exception {
        dropUnit = DropFactory.createDropUnit("test-delete-exception", "DELETE",
                Response.Status.BAD_REQUEST, MediaType.APPLICATION_XML_TYPE, null);

        HttpResponse delivery = executeDropDelivery(dropUnit);
        assertEquals(200, delivery.getStatusLine().getStatusCode());
        String deliveryBody = EntityUtils.toString(delivery.getEntity(), "UTF-8");
        assertNotNull(deliveryBody);
        assertThat(deliveryBody, containsString("droppy registered"));

        count = executeRetrieveCount("delete");
    }

    @Test
    public void shouldTestWithException() throws Exception {
        HttpResponse response = executeBasicHttpDelete(ENDPOINT_HOST + dropUnit.getUrl());
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());

        assertThat(count + 1, is(executeRetrieveCount("delete")));
    }
}
