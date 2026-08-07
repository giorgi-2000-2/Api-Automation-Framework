package ge.gmikeladze.platzi.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * კონფიგურაციის წამკითხავი.
 *
 * FIX D-5: ადრე ფაილი იკითხებოდა ფარდობითი გზით — Files.newInputStream(Paths.get("config.properties")).
 *          ეს დამოკიდებული იყო working directory-ზე და მუშაობდა მხოლოდ იმიტომ, რომ pom.xml-ში
 *          surefire-ს <workingDirectory> ხელით ჰქონდა დაყენებული. ახლა ფაილი იკითხება classpath-იდან
 *          (src/main/resources/config.properties), ანუ IDE-დან, Maven-იდან და jar-იდანაც ერთნაირად მუშაობს.
 *
 * FIX D-4: ადრე get() კითხულობდა მხოლოდ ფაილს, ამიტომ pom.xml-ის <systemPropertyVariables><BASE_URL>
 *          მკვდარი კონფიგი იყო და `mvn test -DBASE_URL=...` არაფერს ცვლიდა.
 *          ახლა პრიორიტეტია: System property (-D) → environment variable → config.properties.
 */
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
            // FIX D-5: აღარ არის e.printStackTrace() + throw (ორმაგი ლოგირება);
            //          თავდაპირველი მიზეზი ინახება cause-ად.
            throw new IllegalStateException(CONFIG_FILE + "-ის წაკითხვა ვერ მოხერხდა", e);
        }
    }

    private ConfigReader() {
        // utility კლასი — ინსტანცირება არ არის საჭირო
    }

    /**
     * მნიშვნელობის წაკითხვა პრიორიტეტების მიხედვით:
     * 1. System property   (მაგ. mvn test -DBASE_URL=https://staging.example.com)
     * 2. Environment variable (CI/CD-ის secret-ები და გარემოს ცვლადები)
     * 3. config.properties (default მნიშვნელობა)
     */
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

    /**
     * იგივე პრიორიტეტი, მაგრამ int-ად. არავალიდურ მნიშვნელობაზე მკაფიო შეცდომას აგდებს,
     * NumberFormatException-ის მაგივრად, რომელიც არ ამბობს რომელი გასაღები გატყდა.
     */
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

    /** ცარიელი ან მხოლოდ ჰარებისგან შემდგარი მნიშვნელობა არ ჩაითვლება მითითებულად. */
    private static boolean isUsable(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
