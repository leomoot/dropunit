package net.lisanza.dropunit.impl.rest.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.InternalServerErrorException;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DigestUtil.class);

    private static final String DIGEST_METHOD_URL = "md5(url/method/request): ";
    private static final String DIGEST_BODY = "md5(body): ";
    private static final String DIGEST_FAILED = "failed!";

    public static String digestEndpoint(String method, String url, String requestInfo)
            throws InternalServerErrorException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(method.getBytes());
            md.update(url.getBytes());
            md.update(requestInfo.replaceAll("\\s+","").getBytes());
            byte[] digest = md.digest();
            String md5 = DatatypeConverter.printHexBinary(digest).toUpperCase();
            LOGGER.debug("{}{} -> {}:{}", DIGEST_METHOD_URL, md5, method, url);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("{}{}", DIGEST_METHOD_URL, DIGEST_FAILED);
            throw new InternalServerErrorException(DIGEST_BODY + DIGEST_FAILED);
        }
    }

    public static String digestRequestBody(String body)
            throws InternalServerErrorException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(body.replaceAll("\\s+","").getBytes());
            byte[] digest = md.digest();
            String md5 = DatatypeConverter.printHexBinary(digest).toUpperCase();
            LOGGER.debug("{}{}", DIGEST_BODY, md5);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("{}{}", DIGEST_METHOD_URL, DIGEST_FAILED);
            throw new InternalServerErrorException(DIGEST_BODY + DIGEST_FAILED);
        }
    }

}
