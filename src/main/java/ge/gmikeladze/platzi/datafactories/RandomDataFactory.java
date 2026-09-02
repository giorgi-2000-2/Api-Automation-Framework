package ge.gmikeladze.platzi.datafactories;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.utils.config.IConfigForData;
import net.datafaker.Faker;

import java.util.UUID;

@Singleton
public class RandomDataFactory {
    private final Faker faker = new Faker();
    private final IConfigForData config;

    @Inject
    public RandomDataFactory(IConfigForData config) {
        this.config = config;
    }

    public Integer randomInt(int numb1, int numb2) {
        return faker.random().nextInt(numb1, numb2);
    }

    public String uniqueTitle(String base) {
        return base + "-" + UUID.randomUUID();
    }

    public String avatar() {
        return config.userAvatar();
    }

    public String validEmail() {
        return "user" + randomInt(10000, 99999) + "@gmail.com";
    }

    public String validUserName() {
        return uniqueTitle("giorgi");
    }

    public String validPassword() {
        return "Pass" + randomInt(1000, 9999);
    }

    public String image() {
        return config.categoryImage();
    }

    public String validTitle() {
        return uniqueTitle(config.categoryName());
    }
}