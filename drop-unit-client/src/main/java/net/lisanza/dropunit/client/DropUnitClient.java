package net.lisanza.dropunit.client;

import net.lisanza.dropunit.impl.rest.DropUnitDto;

import javax.ws.rs.core.Response;
import java.io.IOException;

public class DropUnitClient {

    private final BaseDropUnitClient baseClient;

    public DropUnitClient(String baseUrl) {
        this.baseClient = new BaseDropUnitClient(baseUrl);
    }

    public ClientDropUnitDto drop(String uri, String method,
                                  Response.Status responseCode,
                                  String responseContentType,
                                  String responseBodyFile,
                                  int responseDelay)
            throws IOException {

        DropUnitDto dropUnitDto = DropFactory.createDropUnit(uri, method,
                null, null,
                responseCode, responseContentType, responseBodyFile, responseDelay);

        return new ClientDropUnitDto()
                .withId(baseClient.executeDropDelivery(dropUnitDto))
                .withCount(baseClient.executeRetrieveCount(dropUnitDto.getMethod()))
                .withDropUnitDto(dropUnitDto);
    }

    public ClientDropUnitDto drop(String uri, String method,
                                  Response.Status responseCode,
                                  String responseContentType,
                                  String responseBodyFile)
            throws IOException {
        DropUnitDto dropUnitDto = DropFactory.createDropUnit(uri, method,
                responseCode, responseContentType, responseBodyFile, 0);

        return new ClientDropUnitDto()
                .withId(baseClient.executeDropDelivery(dropUnitDto))
                .withCount(baseClient.executeRetrieveCount(dropUnitDto.getMethod()))
                .withDropUnitDto(dropUnitDto);
    }

    public ClientDropUnitDto drop(String uri, String method,
                                  String requestContentType,
                                  String requestBodyFile,
                                  Response.Status responseCode,
                                  String responseContentType,
                                  String responseBodyFile)
            throws IOException {
        DropUnitDto dropUnitDto = DropFactory.createDropUnit(uri, method,
                requestContentType, requestBodyFile,
                responseCode, responseContentType, responseBodyFile, 0);

        return new ClientDropUnitDto()
                .withId(baseClient.executeDropDelivery(dropUnitDto))
                .withCount(baseClient.executeRetrieveCount(dropUnitDto.getMethod()))
                .withDropUnitDto(dropUnitDto);
    }

    public ClientDropUnitDto drop(String uri, String method,
                                  String requestContentType,
                                  String requestBodyFile,
                                  Response.Status responseCode,
                                  String responseContentType,
                                  String responseBodyFile,
                                  int responseDelay)
            throws IOException {
        DropUnitDto dropUnitDto = DropFactory.createDropUnit(uri, method,
                requestContentType, requestBodyFile,
                responseCode, responseContentType, responseBodyFile, responseDelay);

        return new ClientDropUnitDto()
                .withId(baseClient.executeDropDelivery(dropUnitDto))
                .withCount(baseClient.executeRetrieveCount(dropUnitDto.getMethod()))
                .withDropUnitDto(dropUnitDto);
    }

    public int executeRetrieveCount(ClientDropUnitDto dropUnitDto)
            throws IOException {
        return baseClient.executeRetrieveCount(dropUnitDto.getDropUnitDto().getMethod());
    }
}
