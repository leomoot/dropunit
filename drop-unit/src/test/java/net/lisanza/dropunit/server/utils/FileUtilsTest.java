package net.lisanza.dropunit.server.utils;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FileUtilsTest {

    private static final String TEST_XML_CRLF = "src/test/resources/test-crlf.xml";
    private static final String TEST_XML_LF = "src/test/resources/test-lf.xml";
    private static final String TEST_XML_CR = "src/test/resources/test-cr.xml";

    @Test
    public void shouldReadListWithCRLF() throws Exception {
        List<String> list = FileUtils.readListFromXml(new File(TEST_XML_CRLF));
        assertNotNull(list);
        assertEquals(4, list.size());
        assertTrue(list.get(0).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"));
        assertTrue(list.get(1).startsWith("<pallet>"));
        assertTrue(list.get(1).endsWith("<pallet>"));
        assertFalse(list.get(1).endsWith("<pallet>\r\n"));
        assertFalse(list.get(1).endsWith("<pallet>\r"));
        assertFalse(list.get(1).endsWith("<pallet>\n"));
        assertTrue(list.get(2).startsWith("<bag>droppy</bag>"));
        assertTrue(list.get(2).endsWith("<bag>droppy</bag>"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\r\n"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\r"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\n"));
        assertTrue(list.get(3).startsWith("</pallet>"));
        assertTrue(list.get(3).endsWith("</pallet>"));
        assertFalse(list.get(3).endsWith("</pallet>\r\n"));
        assertFalse(list.get(3).endsWith("</pallet>\r"));
        assertFalse(list.get(3).endsWith("</pallet>\n"));
    }

    @Test
    public void shouldReadListWithLF() throws Exception {
        List<String> list = FileUtils.readListFromXml(new File(TEST_XML_LF));
        assertNotNull(list);
        assertEquals(4, list.size());
        assertTrue(list.get(0).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"));
        assertTrue(list.get(1).startsWith("<pallet>"));
        assertTrue(list.get(1).endsWith("<pallet>"));
        assertFalse(list.get(1).endsWith("<pallet>\r\n"));
        assertFalse(list.get(1).endsWith("<pallet>\r"));
        assertFalse(list.get(1).endsWith("<pallet>\n"));
        assertTrue(list.get(2).startsWith("<bag>droppy</bag>"));
        assertTrue(list.get(2).endsWith("<bag>droppy</bag>"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\r\n"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\r"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\n"));
        assertTrue(list.get(3).startsWith("</pallet>"));
        assertTrue(list.get(3).endsWith("</pallet>"));
        assertFalse(list.get(3).endsWith("</pallet>\r\n"));
        assertFalse(list.get(3).endsWith("</pallet>\r"));
        assertFalse(list.get(3).endsWith("</pallet>\n"));
    }

    @Test
    public void shouldReadListWithCR() throws Exception {
        List<String> list = FileUtils.readListFromXml(new File(TEST_XML_CR));
        assertNotNull(list);
        assertEquals(4, list.size());
        assertTrue(list.get(0).startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertTrue(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r"));
        assertFalse(list.get(0).endsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"));
        assertTrue(list.get(1).startsWith("<pallet>"));
        assertTrue(list.get(1).endsWith("<pallet>"));
        assertFalse(list.get(1).endsWith("<pallet>\r\n"));
        assertFalse(list.get(1).endsWith("<pallet>\r"));
        assertFalse(list.get(1).endsWith("<pallet>\n"));
        assertTrue(list.get(2).startsWith("<bag>droppy</bag>"));
        assertTrue(list.get(2).endsWith("<bag>droppy</bag>"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\r\n"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\r"));
        assertFalse(list.get(2).endsWith("<bag>droppy</bag>\n"));
        assertTrue(list.get(3).startsWith("</pallet>"));
        assertTrue(list.get(3).endsWith("</pallet>"));
        assertFalse(list.get(3).endsWith("</pallet>\r\n"));
        assertFalse(list.get(3).endsWith("</pallet>\r"));
        assertFalse(list.get(3).endsWith("</pallet>\n"));
    }
}
