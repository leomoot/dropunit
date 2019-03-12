package net.lisanza.dropunit.engineundertest.controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Produces(MediaType.APPLICATION_XML)
@Path("/")
public class ProxyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyController.class);

    private String proxyUrl;

    public ProxyController(final String proxyUrl) {
        this.proxyUrl = proxyUrl;
    }

    @GET
    @Path("{any: .*}")
    public Response proxyGet(@Context HttpServletRequest request) {
        LOGGER.info("GET: {}", request.getRequestURI());
        return proxy(request, new HttpGet());
    }

    @POST
    @Path("{any: .*}")
    public Response proxyPost(@Context HttpServletRequest request,
                              String content) {
        LOGGER.info("POST: {}", request.getRequestURI());
        return proxy(request, new HttpPost(), content);
    }

    @PUT
    @Path("{any: .*}")
    public Response proxyPut(@Context HttpServletRequest request,
                             String content) {
        LOGGER.info("PUT: {}", request.getRequestURI());
        return proxy(request, new HttpPut(), content);
    }

    @DELETE
    @Path("{any: .*}")
    public Response proxyDelete(@Context HttpServletRequest request,
                                String content) {
        LOGGER.info("DELETE: {}", request.getRequestURI());
        return proxy(request, new HttpDelete());
    }

    private Response proxy(HttpServletRequest request,
                          HttpEntityEnclosingRequestBase method,
                          String content) {
        StringEntity entity = new StringEntity(content, "UTF-8");
        method.setEntity(entity);
        return proxy(request, method);
    }

    private Response proxy(HttpServletRequest request,
                          HttpRequestBase method) {
        LOGGER.info("proxy uri       : '{}'", request.toString());

        try {
            method.setURI(new URI(proxyUrl + constructUrl(request)));
            LOGGER.info("proxy method     : '{}'", method.toString());
            if (request.getHeader("Content-type") != null) {
                LOGGER.info("proxy contenttype: '{}'", request.getHeader("Content-type"));
                method.setHeader("Content-type", request.getHeader("Content-type"));
            }
            HttpClient client = HttpClientBuilder.create().build();

            return buildProxyResponse(client.execute(method));
        } catch (URISyntaxException e) {
            LOGGER.info(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            LOGGER.info(e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String constructUrl(HttpServletRequest request) {
        if ((request.getQueryString() == null) || request.getQueryString().isEmpty()) {
            return request.getPathInfo();
        } else {
            return request.getPathInfo() + "?" + request.getQueryString();
        }
    }

    private Response buildProxyResponse(HttpResponse response) throws IOException {
        String body = EntityUtils.toString(response.getEntity(), "UTF-8");
        LOGGER.info("build response: {}", response.getStatusLine());
        LOGGER.info("build response: {}", body);

        return Response.status(Response.Status.fromStatusCode(response.getStatusLine().getStatusCode()))
                .entity(body)
                .build();
    }
}

