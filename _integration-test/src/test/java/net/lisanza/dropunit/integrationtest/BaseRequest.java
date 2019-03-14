package net.lisanza.dropunit.integrationtest;

import net.lisanza.dropunit.client.BaseHttpClient;
import net.lisanza.dropunit.client.DropUnitClient;

public class BaseRequest {

    protected static final String ENDPOINT_HOST = "http://127.0.0.1:8080/";
    private static final String DROP_UNIT_HOST = "http://127.0.0.1:9080/";


    protected static final BaseHttpClient httpClient = new BaseHttpClient(ENDPOINT_HOST);
    protected static final DropUnitClient dropUnitClient = new DropUnitClient(DROP_UNIT_HOST);

}