package net.lisanza.dropunit.integrationtest.httpget;

import net.lisanza.dropunit.client.DropFactory;
import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class GetWithQueryStringTestIT extends BaseRequest {

    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    private DropUnitDto dropUnit;
    private int count;

    @Before
    public void setUp() throws Exception {
        dropUnit = DropFactory.createDropUnit("test-get/with/path?and=variables", "GET",
                Response.Status.OK, MediaType.APPLICATION_XML, RESPONSE_FILE);

        dropUnitClient.executeDropDelivery(dropUnit);

        count = dropUnitClient.executeRetrieveCount("get");
    }

    @Test
    public void shouldTestWithQueryString() throws Exception {
        HttpResponse response = httpClient.executeBasicHttpGet(dropUnit.getUrl());
        assertEquals(200, response.getStatusLine().getStatusCode());

        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        assertNotNull(body);
        assertThat(body, containsString(dropUnit.getResponseBody()));

        assertThat(count + 1, is(dropUnitClient.executeRetrieveCount("get")));
    }
}
