package ge.gmikeladze.platzi.utils.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

final class ConfigSource {

    private ConfigSource() {}

    static Properties fromClasspath(String fileName) {
        Properties props = new Properties();
        try (InputStream in = ConfigSource.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                throw new IllegalStateException(
                        fileName + " ვერ მოიძებნა classpath-ზე "+  fileName);
            }
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new IllegalStateException(fileName + "-ის წაკითხვა ვერ მოხერხდა", e);
        }
    }
}