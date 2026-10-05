package ge.gmikeladze.platzi.datafactories;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.request.LoginRequestDto;
import ge.gmikeladze.platzi.utils.config.IConfigForData;

@Singleton
public class AuthDataFactory {
    private final IConfigForData config;
@Inject
    public AuthDataFactory(IConfigForData config) {
        this.config = config;
    }

    public LoginRequestDto validLogin() {
        return LoginRequestDto.builder()
                .email(config.email())
                .password(config.password())
                .build();
    }

    public LoginRequestDto loginWith(String email, String password) {
        return LoginRequestDto.builder().email(email).password(password).build();
    }
}