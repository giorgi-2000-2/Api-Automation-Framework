package ge.gmikeladze.platzi.datafactories;

import com.google.inject.Singleton;
import net.datafaker.Faker;

import java.util.UUID;


@Singleton
public class RandomDataFactory {

    private final Faker faker = new Faker();

    public Integer randomInt(int numb1, int numb2) {
        return faker.random().nextInt(numb1, numb2);
    }

    public String uniqueTitle(String base) {
        return base + "-" + UUID.randomUUID();
    }

}
