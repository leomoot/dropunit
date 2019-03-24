package net.lisanza.dropunit.impl.rest.services;

import net.lisanza.dropunit.impl.rest.DropUnitDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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

        String dropId = digestEndpointId(dropUnitDto);
        LOGGER.debug("register {} - {}", dropId, dropUnitDto);

        registrations.put(dropId, new DropUnitEndpoint()
                .withId(dropId)
                .withDropUnitDto(dropUnitDto));
        return dropId;
    }

    public DropUnitEndpoint lookupEndpoint(DropUnitDto dropUnitDto) {
        if (dropUnitDto == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }

        String dropId = digestEndpointId(dropUnitDto);
        LOGGER.debug("lookupEndpoint {} - {}", dropId, dropUnitDto);
        return lookupEndpoint(dropId);
    }

    public DropUnitEndpoint lookupEndpoint(String dropId) {
        DropUnitEndpoint endpoint = registrations.get(dropId);
        if (endpoint == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }
        return endpoint;
    }

    private String digestEndpointId(DropUnitDto dropUnitDto)
            throws InternalServerErrorException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(dropUnitDto.getUrl().getBytes());
            md.update(dropUnitDto.getMethod().getBytes());
            byte[] digest = md.digest();
            String md5 = DatatypeConverter.printHexBinary(digest).toUpperCase();
            LOGGER.info("md5 : {}", md5);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            String msg = "'drop unit' md5 failed!";
            LOGGER.error(msg);
            throw new InternalServerErrorException(msg);
        }
    }


}
