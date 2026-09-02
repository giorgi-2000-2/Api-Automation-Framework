package ge.gmikeladze.platzi.datafactories;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.request.CreateUserDto;
import ge.gmikeladze.platzi.dtos.request.UpdateUserDto;
import ge.gmikeladze.platzi.utils.config.IConfigForData;


@Singleton
public class UserDataFactory {
    private final RandomDataFactory randomDataFactory;
    private final IConfigForData config;
    @Inject
    public UserDataFactory(RandomDataFactory randomDataFactory, IConfigForData config) {
        this.randomDataFactory = randomDataFactory;
        this.config = config;
    }

    public CreateUserDto createUserWithData() {
        return CreateUserDto.builder()
                .email(randomDataFactory.validEmail())
                .name(randomDataFactory.validUserName())
                .password(randomDataFactory.validPassword())
                .role("admin")
                .avatar(config.userAvatar())
                .build();
    }

    public UpdateUserDto updateUserWithData() {
        return UpdateUserDto.builder()
                .email("updated" + randomDataFactory.validEmail())
                .name("updated" + randomDataFactory.validUserName())
                .password("updated" + randomDataFactory.validPassword())
                .role("admin")
                .avatar(config.userAvatar())
                .build();
    }
}