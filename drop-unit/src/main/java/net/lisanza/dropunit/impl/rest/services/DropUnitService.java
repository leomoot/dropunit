package net.lisanza.dropunit.impl.rest.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static net.lisanza.dropunit.impl.rest.services.DigestUtil.digestEndpoint;

public class DropUnitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DropUnitService.class);

    private Hashtable<String, DropUnitEndpoint> registrations = new Hashtable<>();

    public String dropAll() {
        registrations.clear();
        return "droppy dropped";
    }

    public List<DropUnitEndpoint> getAll() {
        List<DropUnitEndpoint> list = new ArrayList<>();
        for (DropUnitEndpoint droppy : registrations.values()) {
            list.add(droppy);
        }
        return list;
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
        DropUnitEndpoint endpoint = registrations.get(dropId);
        if (endpoint == null) {
            LOGGER.warn("no endpoint registered for {}:{}", dto.getMethod(), dto.getUrl());
            throw new NotFoundException("no endpoint registered for " + dto.getMethod() + ":" + dto.getUrl());
        }
        return endpoint;
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
}
