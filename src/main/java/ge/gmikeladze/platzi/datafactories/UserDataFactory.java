package ge.gmikeladze.platzi.datafactories;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.request.CreateUserDto;
import ge.gmikeladze.platzi.dtos.request.UpdateUserDto;
import ge.gmikeladze.platzi.utils.ConfigReader;

@Singleton
public class UserDataFactory {
    private final RandomDataFactory randomDataFactory;
    @Inject
    public UserDataFactory(RandomDataFactory randomDataFactory) {
        this.randomDataFactory = randomDataFactory;
    }

    public CreateUserDto createUserWithData() {
        return CreateUserDto.builder()
                .email(randomDataFactory.validEmail())
                .name(randomDataFactory.validUserName())
                .password(randomDataFactory.validPassword())
                .role("admin")
                .avatar(ConfigReader.get("user.avatar"))
                .build();
    }

    public UpdateUserDto updateUserWithData() {
        return UpdateUserDto.builder()
                .email("updated" + randomDataFactory.validEmail())
                .name("updated" + randomDataFactory.validUserName())
                .password("updated" + randomDataFactory.validPassword())
                .role("admin")
                .avatar(ConfigReader.get("user.avatar"))
                .build();
    }
}