package net.lisanza.dropunit.client;

import net.lisanza.dropunit.impl.rest.DropUnitDto;

import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class DropFactory {

    public static DropUnitDto createDropUnit(String uri, String method,
                                             String requestContentType,
                                             String requestBodyFile,
                                             Response.Status responseCode,
                                             String responseContentType,
                                             String responseBodyFile,
                                             int responseDelay)
            throws IOException {
        DropUnitDto dropUnitDto = new DropUnitDto();
        dropUnitDto.setUrl(uri);
        dropUnitDto.setMethod(method);
        if (requestContentType != null) {
            dropUnitDto.getRequestBodyInfo().setRequestContentType(requestContentType);
        }
        if (requestBodyFile != null) {
            dropUnitDto.getRequestBodyInfo().setRequestBody(readFromFile(requestBodyFile));
        }
        dropUnitDto.getResponseBodyInfo().setResponseCode(responseCode.getStatusCode());
        dropUnitDto.getResponseBodyInfo().setResponseContentType(responseContentType);
        if (responseBodyFile != null) {
            dropUnitDto.getResponseBodyInfo().setResponseBody(readFromFile(responseBodyFile));
        }
        dropUnitDto.setResponseDelay(responseDelay);
        return dropUnitDto;
    }

    public static String readFromFile(String fileName) throws IOException {
        try (InputStream inputStream = new FileInputStream(new File(fileName))) {
            String result = new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
            return result;
        }
    }
}
    
