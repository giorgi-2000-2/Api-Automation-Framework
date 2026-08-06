package org.example.utils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader  {
    private static final Properties properties = new Properties();


    static {
        try (InputStream inputStream = Files.newInputStream(Paths.get("config.properties"))) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load config.properties file!");
        }
    }


    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }


}