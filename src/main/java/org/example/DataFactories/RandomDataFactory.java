package org.example.DataFactories;
import com.github.javafaker.Faker;
import java.util.UUID;

public class RandomDataFactory {

    public int randomInt(int numb){
        Faker faker = new Faker();
        return faker.random().nextInt(0,numb);
    }
    public Integer randomInt(int numb1, int numb2){
        Faker faker = new Faker();
        return faker.random().nextInt(numb1,numb2);
    }



    public String uniqueTitle(String base) {
        return base + "-" + UUID.randomUUID().toString();
    }


}
