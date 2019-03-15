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
        LOGGER.debug("register {} - {}", dropId, dropUnitDto);

        registrations.put(md5(dropUnitDto), dropUnitDto);
        return "droppy registered";
    }

    public DropUnitDto lookup(DropUnitDto dropUnitDto) {
        if (dropUnitDto == null) {
            String msg = "'drop unit' is missing!";
            LOGGER.error(msg);
            throw new BadRequestException(msg);
        }
        LOGGER.debug("lookup - {}", dropUnitDto);

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
