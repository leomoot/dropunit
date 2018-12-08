package net.lisanza.dropunit.simulator.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class AppUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppUtils.class);

    public static String loadFromFile(String file) {
        LOGGER.info("returning file: {}", file);
        try (InputStream inputStream = new FileInputStream(new File(file))) {
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(String.format("loadFromFile(%s) : %s", file, e.getMessage()));
            return "{}";
        }
    }

    public static String generateJsonErrorMessageStr(String message) {
        return String.format("{\"Error message\": \"%s\"}", message);
    }

    public static Document xmlStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            LOGGER.error(String.format("xmlStringToDocument(%s) : %s", xmlStr, e.getMessage()));
        }
        return null;
    }

}
