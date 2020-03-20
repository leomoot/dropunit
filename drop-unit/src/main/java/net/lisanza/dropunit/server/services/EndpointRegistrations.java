package net.lisanza.dropunit.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;

public class EndpointRegistrations extends ArrayList<DropUnitEndpoint> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndpointRegistrations.class);

    public List<DropUnitEndpoint> findByUrlAndMethod(String url, String method) {
        List<DropUnitEndpoint> subList = new ArrayList<>();
        for (DropUnitEndpoint endpoint : this) {
            LOGGER.debug("findByUrlAndMethod: {} <> {} | {} <> {}",
                    url, endpoint.getUrl(),
                    method, endpoint.getMethod());

            if (matchesUrl(url, endpoint.getUrl())
                    && matchesMethod(method, endpoint.getMethod())) {
                subList.add(endpoint);
            }
        }
        LOGGER.info("findByUrlAndMethod: {}", subList.size());
        return subList;
    }

    public DropUnitEndpoint findById(String dropId) {
        if (dropId == null || dropId.isEmpty()) {
            LOGGER.warn("'dropId' is missing!");
            throw new BadRequestException("'dropId' is missing!");
        }
        DropUnitEndpoint endpoint = findInList(dropId);
        if (endpoint == null) {
            LOGGER.warn("no endpoint registered for {}", dropId);
            throw new NotFoundException("no endpoint registered for " + dropId);
        }
        return endpoint;
    }

    public DropUnitEndpoint findInList(String dropId) {
        for (DropUnitEndpoint endpoint : this) {
            if (dropId.equals(endpoint.getId())) {
                return endpoint;
            }
        }
        return null;
    }

    private boolean matchesUrl(String requestUrl, String endpointUrl) {
        if (endpointUrl.endsWith("?")) {
            return requestUrl.startsWith(endpointUrl);
        }
        return requestUrl.equals(endpointUrl);
    }

    private boolean matchesMethod(String method, String endpointMethod) {
        return method.equals(endpointMethod);
    }
}