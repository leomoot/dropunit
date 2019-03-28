package net.lisanza.dropunit.impl.rest.services;

import net.lisanza.dropunit.impl.rest.DropUnitDto;
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

    public List<DropUnitDto> getAll() {
        List<DropUnitDto> list = new ArrayList<>();
        for (DropUnitEndpoint droppy : registrations.values()) {
            list.add(droppy.getDropUnitDto());
        }
        return list;
    }

    public String register(DropUnitDto dropUnitDto) {
        if (dropUnitDto == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }
        if (!dropUnitDto.getUrl().startsWith("/")) {
            dropUnitDto.setUrl("/" + dropUnitDto.getUrl());
        }

        String dropId = digestEndpoint(dropUnitDto.getMethod(), dropUnitDto.getUrl());
        LOGGER.debug("register {} - {}", dropId, dropUnitDto);

        registrations.put(dropId, new DropUnitEndpoint()
                .withId(dropId)
                .withDropUnitDto(dropUnitDto));
        return dropId;
    }

    public DropUnitEndpoint deregister(String dropId) {
        DropUnitEndpoint endpoint = lookupEndpoint(dropId);
        if (endpoint != null) {
            registrations.remove(dropId);
        }
        return endpoint;
    }


    public DropUnitEndpoint lookupEndpoint(DropUnitDto dropUnitDto) {
        if (dropUnitDto == null) {
            LOGGER.warn("'drop unit' is missing!");
            throw new BadRequestException("'drop unit' is missing!");
        }
        String dropId = digestEndpoint(dropUnitDto.getMethod(), dropUnitDto.getUrl());
        DropUnitEndpoint endpoint = registrations.get(dropId);
        if (endpoint == null) {
            LOGGER.warn("no endpoint registered for {}:{}", dropUnitDto.getMethod(), dropUnitDto.getUrl());
            throw new NotFoundException("no endpoint registered for " + dropUnitDto.getMethod() + ":" + dropUnitDto.getUrl());
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
