package net.lisanza.dropunit.integrationtest.httpdelete;

import net.lisanza.dropunit.impl.rest.DropUnitDto;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import net.lisanza.dropunit.integrationtest.DropFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.SocketTimeoutException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DeleteWithConnectionTimeoutTestIT extends BaseRequest {

    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    private DropUnitDto dropUnit;
    private int count;

    @Before
    public void setUp() throws Exception {
        dropUnit = DropFactory.createDropUnit("test-delete", "DELETE",
                Response.Status.GATEWAY_TIMEOUT, MediaType.APPLICATION_XML_TYPE, RESPONSE_FILE,
                20000);

        HttpResponse delivery = executeDropDelivery(dropUnit);
        assertEquals(200, delivery.getStatusLine().getStatusCode());
        String deliveryBody = EntityUtils.toString(delivery.getEntity(), "UTF-8");
        assertNotNull(deliveryBody);
        assertThat(deliveryBody, containsString("droppy registered"));

        count = executeRetrieveCount("delete");
    }

    @Test
    public void shouldTestWithConnectionTimeout() throws Exception {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(1000)
                .setConnectTimeout(1000)
                .setSocketTimeout(1000)
                .build();
        try {
            executeBasicHttpDelete(ENDPOINT_HOST + dropUnit.getUrl(),
                    requestConfig);
            assertTrue(false);
        } catch (SocketTimeoutException e) {
            assertTrue(true);
        }

        assertThat(count + 1, is(executeRetrieveCount("delete")));
    }
}
