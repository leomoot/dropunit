package net.lisanza.dropunit.impl.rest.constants;

public final class RequestMappings {

    // config service
    public static final String DROP_UNIT_SERVICE = "/dropunit";

    public static final String URI_DELIVERY_ENDPOINT = "/delivery/endpoint";
    public static final String URI_DELIVERY_ENDPOINT_DROPID = "/delivery/endpoint/{dropId}";
    public static final String URI_DELIVERY_ENDPOINT_DROPID_REQUESTBODY = "/delivery/endpoint/{dropId}/request-body";
    public static final String URI_DELIVERY_ENDPOINT_DROPID_RESPONSEBODY_STATUS = "/delivery/endpoint/{dropId}/response-body/{status}";
    public static final String URI_RECIEVED_DROPID_NUMBER = "/recieved/{dropId}/{number}";
    public static final String URI_COUNT_DROPID = "/count/{dropId}";
    public static final String URI_CLEARALLDROPS = "/clearAllDrops";
    public static final String URI_GETALLDROPS = "/getAllDrops";
    public static final String URI_GETALLNOTFOUNDS = "/getAllNotFounds";
    public static final String URI_COUNT_NOTFOUND = "/count/notFound";

    // root Service
    public static final String ROOT_SERVICE = "/";
}
