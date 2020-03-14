package net.lisanza.dropunit.server.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

    public static String readFromFile(String fileName) throws IOException {
        return readFromFile(new File(fileName));
    }

    public static String readFromFile(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining("\n"));
        }
    }

    public static List<String> readListFromXml(String fileName) throws IOException {
        return readListFromXml(new File(fileName));
    }

    public static List<String> readListFromXml(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .map(x -> x.trim())
                    .collect(Collectors.toList());
        }
    }
}
