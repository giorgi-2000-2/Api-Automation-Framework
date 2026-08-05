package org.example.DataFactories;
import com.github.javafaker.Faker;
import java.util.UUID;

public class RandomDataFactory {
    Faker faker = new Faker();
    public int randomInt(int numb){

        return faker.random().nextInt(0,numb);
    }
    public Integer randomInt(int numb1, int numb2){

        return faker.random().nextInt(numb1,numb2);
    }



    public String uniqueTitle(String base) {
        return base + "-" + UUID.randomUUID().toString();
    }


    public int getWrongNumber(){
        return randomInt(-1,0);
    }
}
