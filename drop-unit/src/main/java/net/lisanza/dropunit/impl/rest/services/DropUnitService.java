package net.lisanza.dropunit.impl.rest.services;

import net.lisanza.dropunit.impl.rest.services.data.EndpointNotFound;
import net.lisanza.dropunit.impl.rest.services.data.ReceivedRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class DropUnitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropUnitService.class);

    private EndpointRegistrations registrations = new EndpointRegistrations();

    private Hashtable<String, DropUnitEndpoint> defaults = new Hashtable<>();
    private Hashtable<String, EndpointNotFound> notFound = new Hashtable<>();

    public Collection<DropUnitEndpoint> getAllDefaults() {
        return defaults.values();
    }

    public Collection<DropUnitEndpoint> getAllRegistrations() {
        return registrations;
    }

    public Collection<EndpointNotFound> getAllNotFound() {
        return notFound.values();
    }

    public String dropAll() {
        StringBuilder stringBuilder = new StringBuilder();
        infoLoadedEndpoints(stringBuilder.append("endpoints: "));
        registrations.clear();
        notFound.clear();
        registrations.addAll(defaults.values());
        infoLoadedEndpoints(stringBuilder.append(" -> after: "));
        return stringBuilder.toString();
    }

    public String registerDefault(DropUnitEndpoint endpoint) {
        String dropId = generateDropId(endpoint);
        endpoint.setId(dropId);
        defaults.put(dropId, endpoint);
        return dropId;
    }

    public String register(DropUnitEndpoint endpoint) {
        String dropId = generateDropId(endpoint);
        endpoint.setId(dropId);
        registrations.add(endpoint);
        return dropId;
    }

    public String generateDropId(DropUnitEndpoint endpoint) {
        if (endpoint == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }
        if (!endpoint.getUrl().startsWith("/")) {
            endpoint.setUrl("/" + endpoint.getUrl());
        }

        String dropId = Integer.toString(endpoint.hashCode());
        LOGGER.debug("register {} - {}", dropId, endpoint);
        return dropId;
    }

    public DropUnitEndpoint deregister(String dropId) {
        for (DropUnitEndpoint registration: registrations) {
            if ((registration.getId() != null) && (registration.getId().equals(dropId))) {
                registrations.remove(dropId);
                return registration;
            }
        }
        return null;
    }

    public String registerRequest(String dropId, DropUnitEndpointRequest patterns) {
        DropUnitEndpoint endpoint = lookupEndpoint(dropId);
        if (endpoint == null) {
            return "no registration found";
        }
        endpoint.setRequest(patterns);
        return "OK";
    }

    public String registerResponse(String dropId, DropUnitEndpointResponse response) {
        DropUnitEndpoint endpoint = lookupEndpoint(dropId);
        if (endpoint == null) {
            return "no registration found";
        }
        endpoint.setResponse(response);
        return "OK";
    }

    public List<DropUnitEndpoint> lookupEndpoint(String url, String method) {
        if (url == null) {
            LOGGER.warn("'url' is missing!");
            throw new BadRequestException("'url' is missing!");
        }
        if (method == null) {
            LOGGER.warn("'method' is missing!");
            throw new BadRequestException("'method' is missing!");
        }
        return registrations.findByUrlAndMethod(url, method);
    }

    public DropUnitEndpoint lookupEndpoint(String dropId) {
        return registrations.findById(dropId);
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

    // Utils

    public StringBuilder infoLoadedEndpoints(StringBuilder stringBuilder) {
        stringBuilder.append("defaults ").append(defaults.size())
                .append(" registrations ").append(registrations.size())
                .append(" not found ").append(notFound.size());
        return stringBuilder;
    }
}
