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

    private Hashtable<String, DropUnitDto> registrations = new Hashtable<>();

    public String dropAll() {
        registrations.clear();
        return "droppy dropped";
    }

    public List<DropUnitDto> getAll() {
        List<DropUnitDto> list = new ArrayList<>();
        for (DropUnitDto droppy: registrations.values()) {
            list.add(droppy);
        }
        return list;
    }

    public String register(String dropId, DropUnitDto dropUnitDto) {
        if (dropUnitDto == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }
        if (!dropUnitDto.getUrl().startsWith("/")) {
            dropUnitDto.setUrl("/" + dropUnitDto.getUrl());
        }
        LOGGER.debug("register {} - url       : {}", dropId, dropUnitDto.getUrl());
        LOGGER.debug("register {} - method    : {}", dropId, dropUnitDto.getMethod());
        LOGGER.debug("register {} - req-type  : {}", dropId, dropUnitDto.getRequestContentType());
        LOGGER.debug("register {} - req-body  : {}", dropId, dropUnitDto.getRequestBody());
        LOGGER.debug("register {} - resp-delay: {}", dropId, dropUnitDto.getResponseDelay());
        LOGGER.debug("register {} - resp-code : {}", dropId, dropUnitDto.getResponseCode());
        LOGGER.debug("register {} - resp-type : {}", dropId, dropUnitDto.getResponseContentType());
        LOGGER.debug("register {} - resp-body : {}", dropId, dropUnitDto.getResponseBody());

        registrations.put(md5(dropUnitDto), dropUnitDto);
        return "droppy registered";
    }

    public DropUnitDto lookup(DropUnitDto dropUnitDto) {
        if (dropUnitDto == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }

        LOGGER.debug("lookup - url       : {}", dropUnitDto.getUrl());
        LOGGER.debug("lookup - method    : {}", dropUnitDto.getMethod());
        LOGGER.debug("lookup - req-type  : {}", dropUnitDto.getRequestContentType());
        LOGGER.debug("lookup - req-body  : {}", dropUnitDto.getRequestBody());
        LOGGER.debug("lookup - resp-delay: {}", dropUnitDto.getResponseDelay());
        LOGGER.debug("lookup - resp-code : {}", dropUnitDto.getResponseCode());
        LOGGER.debug("lookup - resp-type : {}", dropUnitDto.getResponseContentType());
        LOGGER.debug("lookup - resp-body : {}", dropUnitDto.getResponseBody());

        return registrations.get(md5(dropUnitDto));
    }

    public String md5(DropUnitDto dropUnitDto)
            throws InternalServerErrorException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(dropUnitDto.getUrl().getBytes());
            md.update(dropUnitDto.getMethod().getBytes());
            if (dropUnitDto.getRequestBody() != null) {
                md.update(dropUnitDto.getRequestBody().getBytes());
            }
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
