package ge.gmikeladze.platzi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream =
                     ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        CONFIG_FILE + " ვერ მოიძებნა classpath-ზე. "
                                + "მოსალოდნელი მდებარეობა: src/main/resources/" + CONFIG_FILE);
            }
            properties.load(inputStream);

        } catch (IOException e) {

            throw new IllegalStateException(CONFIG_FILE + "-ის წაკითხვა ვერ მოხერხდა", e);
        }
    }

    private ConfigReader() {

    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (isUsable(systemValue)) {
            return systemValue.trim();
        }

        String envValue = System.getenv(key);
        if (isUsable(envValue)) {
            return envValue.trim();
        }

        return properties.getProperty(key);
    }
    public static int getInt(String key) {
        String value = get(key);
        if (value == null) {
            throw new IllegalStateException("კონფიგის გასაღები ვერ მოიძებნა: " + key);
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "კონფიგის გასაღები \"" + key + "\" არ არის რიცხვი: \"" + value + "\"", e);
        }
    }

    private static boolean isUsable(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
