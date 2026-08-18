package ge.gmikeladze.platzi.datafactories;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.utils.ConfigReader;
import net.datafaker.Faker;

import java.util.UUID;


@Singleton
public class RandomDataFactory {

    private final Faker faker = new Faker();

    protected Integer randomInt(int numb1, int numb2) {
        return faker.random().nextInt(numb1, numb2);
    }

    public String uniqueTitle(String base) {
        return base + "-" + UUID.randomUUID();
    }

    protected String avatar() {
        return ConfigReader.get("avatar");
    }

    protected String validEmail() {
        return "user" + randomInt(10000, 99999) + "@gmail.com";
    }

    protected String validName() {
        return uniqueTitle("giorgi");
    }

    protected String validPassword() {
        return "Pass" + randomInt(1000, 9999);
    }
    protected String image() {
        return ConfigReader.get("categoryImage");
    }

    protected String validTitle() {
        return uniqueTitle(ConfigReader.get("categoryName"));
    }

}
