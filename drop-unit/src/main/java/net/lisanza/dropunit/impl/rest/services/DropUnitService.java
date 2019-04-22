package net.lisanza.dropunit.impl.rest.services;

import net.lisanza.dropunit.impl.rest.services.data.EndpointNotFound;
import net.lisanza.dropunit.impl.rest.services.data.ReceivedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.Hashtable;

import static net.lisanza.dropunit.impl.rest.services.DigestUtil.digestEndpoint;

public  class DropUnitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropUnitService.class);

    private Hashtable<String, DropUnitEndpoint> registrations = new Hashtable<>();
    private Hashtable<String, EndpointNotFound> notFound = new Hashtable<>();

    public String dropAll() {
        int registered = registrations.size();
        int notfound = notFound.size();
        registrations.clear();
        notFound.clear();
        return "drop size " + registered + "-> after: " + registrations.size()
                + " not found " + notfound + "-> after: " + notFound.size();
    }

    public Collection<DropUnitEndpoint> getAllRegistrations() {
        return registrations.values();
    }

    public Collection<EndpointNotFound> getAllNotFound() {
        return notFound.values();
    }

    public String register(DropUnitEndpoint endpoint) {
        if (endpoint == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }
        if (!endpoint.getUrl().startsWith("/")) {
            endpoint.setUrl("/" + endpoint.getUrl());
        }

        String dropId = digestEndpoint(endpoint.getMethod(), endpoint.getUrl());
        LOGGER.debug("register {} - {}", dropId, endpoint);

        registrations.put(dropId, endpoint);
        return dropId;
    }

    public DropUnitEndpoint deregister(String dropId) {
        DropUnitEndpoint endpoint = lookupEndpoint(dropId);
        if (endpoint != null) {
            registrations.remove(dropId);
        }
        return endpoint;
    }

    public String registerRequest(String dropId, AbstractDropUnitRequest patterns) {
        DropUnitEndpoint endpoint = lookupEndpoint(dropId);
        if (endpoint == null) {
            return "no registration found";
        }
        endpoint.setRequest(patterns);
        return "OK";
    }

    public String registerResponse(String dropId, DropUnitResponse response) {
        DropUnitEndpoint endpoint = lookupEndpoint(dropId);
        if (endpoint == null) {
            return "no registration found";
        }
        endpoint.setResponse(response);
        return "OK";
    }

    public DropUnitEndpoint lookupEndpoint(DropUnitEndpoint dto) {
        if (dto == null) {
            LOGGER.warn("'drop unit' is missing!");
            throw new BadRequestException("'drop unit' is missing!");
        }
        String dropId = digestEndpoint(dto.getMethod(), dto.getUrl());
        return registrations.get(dropId);
    }

    public DropUnitEndpoint lookupEndpoint(String dropId) {
        if (dropId == null || dropId.isEmpty()) {
            LOGGER.warn("'dropId' is missing!");
            throw new BadRequestException("'dropId' is missing!");
        }
        DropUnitEndpoint endpoint = registrations.get(dropId);
        if (endpoint == null) {
            LOGGER.warn("no endpoint registered for {}", dropId);
            throw new NotFoundException("no endpoint registered for " + dropId);
        }
        return endpoint;
    }

    public void registerNotFound(String url, ReceivedRequest notFoundRequest) {
        if (url == null) {
            LOGGER.warn("'url' is missing!");
            throw new BadRequestException("'url' is missing!");
        }
        if (notFoundRequest == null) {
            LOGGER.warn("'request' is missing!");
            throw new BadRequestException("'request' is missing!");
        }
        EndpointNotFound notFoundEndpoint = notFound.get(url);
        if (notFoundEndpoint == null) {
            notFound.put(url, new EndpointNotFound()
                    .withUrl(url)
                    .withReceivedRequests(notFoundRequest));
        } else {
            notFoundEndpoint.addReceivedRequests(notFoundRequest);
        }
    }
}
