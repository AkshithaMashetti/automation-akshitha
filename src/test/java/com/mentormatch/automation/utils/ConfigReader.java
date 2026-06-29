package com.mentormatch.automation.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties was not found on the test classpath.");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load config.properties", e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue.trim();
        }
        return PROPERTIES.getProperty(key);
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return value == null || value.isBlank() ? defaultValue : value.trim();
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Integer.parseInt(value.trim());
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value.trim());
    }

    public static String baseUrl() {
        String value = get("baseUrl", "https://mentormatch-green.netlify.app/");
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    public static Path resolvePath(String pathValue) {
        Path path = Paths.get(pathValue);
        if (path.isAbsolute()) {
            return path;
        }

        Path current = Paths.get(System.getProperty("user.dir")).resolve(path).normalize();
        if (Files.exists(current)) {
            return current;
        }

        Path nestedProject = Paths.get(System.getProperty("user.dir")).resolve("automation-tests").resolve(path).normalize();
        if (Files.exists(nestedProject)) {
            return nestedProject;
        }

        return current;
    }
}
