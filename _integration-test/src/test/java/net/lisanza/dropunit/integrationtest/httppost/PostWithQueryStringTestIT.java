package net.lisanza.dropunit.integrationtest.httppost;

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

public class PostWithQueryStringTestIT extends BaseRequest {

    private static final String REQUEST_FILE = "src/test/resources/xml/drop-request.xml";
    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    private DropUnitDto dropUnit;
    private int count;

    @Before
    public void setUp() throws Exception {
        dropUnit = DropFactory.createDropUnit("test-post/with/path?and=variables",
                "POST", MediaType.APPLICATION_XML_TYPE, REQUEST_FILE,
                Response.Status.OK, MediaType.APPLICATION_XML_TYPE, RESPONSE_FILE);

        HttpResponse delivery = executeDropDelivery(dropUnit);
        assertEquals(200, delivery.getStatusLine().getStatusCode());
        String deliveryBody = EntityUtils.toString(delivery.getEntity(), "UTF-8");
        assertNotNull(deliveryBody);
        assertThat(deliveryBody, containsString("droppy registered"));

        count = executeRetrieveCount("post");
    }

    @Test
    public void shouldTestWithQueryString() throws Exception {
        HttpResponse response = executeBasicHttpPost(ENDPOINT_HOST + dropUnit.getUrl(), REQUEST_FILE, XML);
        assertEquals(200, response.getStatusLine().getStatusCode());

        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        assertNotNull(body);
        assertThat(body, containsString(dropUnit.getResponseBody()));

        assertThat(count + 1, is(executeRetrieveCount("post")));
    }

}
