package ge.gmikeladze.platzi.datafactories;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.request.CreateUserDto;
import ge.gmikeladze.platzi.dtos.request.UpdateUserDto;
import ge.gmikeladze.platzi.utils.ConfigReader;

@Singleton
public class UserDataFactory extends RandomDataFactory{

    public CreateUserDto createUserWithData() {
        return CreateUserDto.builder()
                .email(validEmail())
                .name(validName())
                .password(validPassword())
                .role("admin")
                .avatar(ConfigReader.get("avatar"))
                .build();
    }

    public UpdateUserDto updateUserWithData() {
        return UpdateUserDto.builder()
                .email("updated"+validEmail())
                .name("updated"+validName())
                .password("updated"+validPassword())
                .role("admin")
                .avatar(ConfigReader.get("avatar"))
                .build();
    }




}
