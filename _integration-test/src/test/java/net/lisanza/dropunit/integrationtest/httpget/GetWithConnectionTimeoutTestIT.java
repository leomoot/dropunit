package net.lisanza.dropunit.integrationtest.httpget;

import net.lisanza.dropunit.client.ClientDropUnit;
import net.lisanza.dropunit.integrationtest.BaseRequest;
import org.apache.http.client.config.RequestConfig;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.net.SocketTimeoutException;

import static org.junit.Assert.assertTrue;

public class GetWithConnectionTimeoutTestIT extends BaseRequest {

    private static final String RESPONSE_FILE = "src/test/resources/xml/drop-response.xml";

    @Test
    public void shouldTestWithConnectionTimeout() throws Exception {
        ClientDropUnit dropUnit = new ClientDropUnit(DROP_UNIT_HOST)
                .withGet("test-get")
                .withResponseOkFromFile(MediaType.APPLICATION_XML, RESPONSE_FILE)
                .withResponseDelay(20000)
                .drop();

        try {
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(1000)
                    .setConnectTimeout(1000)
                    .setSocketTimeout(1000)
                    .build();
            httpClient.executeBasicHttpGet(dropUnit.getUrl(), requestConfig);
            assertTrue(false);
        } catch (SocketTimeoutException e) {
            assertTrue(true);
        }

        dropUnit.assertCount(1);
    }
}
