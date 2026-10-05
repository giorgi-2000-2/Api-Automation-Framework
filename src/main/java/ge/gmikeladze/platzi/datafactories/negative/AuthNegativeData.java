package ge.gmikeladze.platzi.datafactories.negative;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.AuthDataFactory;
import ge.gmikeladze.platzi.dtos.request.LoginRequestDto;
import ge.gmikeladze.platzi.dtos.response.UnauthorizedErrorDto;
import org.testng.annotations.DataProvider;

import static ge.gmikeladze.platzi.datafactories.negative.NegativeCase.of;

@Singleton
public class AuthNegativeData {
    private final String UNAUTHORIZED = "Unauthorized";

    private final String FOREIGN_SIGNED_JWT =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                    + ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ"
                    + ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    private final String UNSIGNED_JWT =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
                    + ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ";

    private final AuthDataFactory authDataFactory;

    @Inject
    public AuthNegativeData(AuthDataFactory authDataFactory) {
        this.authDataFactory = authDataFactory;
    }

    private String validEmail() {
        return authDataFactory.validLogin().getEmail();
    }

    private String validPassword() {
        return authDataFactory.validLogin().getPassword();
    }

    private NegativeCase<LoginRequestDto> loginCase(String name, String email, String password) {
        return of(name, authDataFactory.loginWith(email, password),
                HttpStatusCode.UNAUTHORIZED, UnauthorizedErrorDto.class, UNAUTHORIZED);
    }

    private NegativeCase<String> tokenCase(String name, String token) {
        return of(name, token,
                HttpStatusCode.UNAUTHORIZED, UnauthorizedErrorDto.class, UNAUTHORIZED);
    }

    @DataProvider(name = "invalidLogin")
    public Object[][] invalidLogin() {
        return new Object[][]{
                {loginCase("password — არასწორი",
                        validEmail(), "wrong-password")},

                {loginCase("password — სხვა რეგისტრით",
                        validEmail(), validPassword().toUpperCase())},

                {loginCase("password — ჰარებით თავში და ბოლოში",
                        validEmail(), " " + validPassword() + " ")},

                {loginCase("password — ცარიელი",
                        validEmail(), "")},

                {loginCase("password — null",
                        validEmail(), null)},

                {loginCase("email — არარსებული მომხმარებელი",
                        "no-such-user-98765@mail.com", validPassword())},

                {loginCase("email — არავალიდური ფორმატი",
                        "not-an-email", validPassword())},

                {loginCase("email — ცარიელი",
                        "", validPassword())},

                {loginCase("email — null",
                        null, validPassword())},

                {loginCase("email და password — ორივე ცარიელი",
                        "", "")},

                {loginCase("email და password — ორივე null",
                        null, null)},

                {loginCase("email და password — SQL injection",
                        "' OR '1'='1", "' OR '1'='1")},
        };
    }

    @DataProvider(name = "invalidToken")
    public Object[][] invalidToken() {
        return new Object[][]{
                {tokenCase("token — შემთხვევითი ტექსტი", "invalid.token.here")},

                {tokenCase("token — ცარიელი", "")},

                {tokenCase("token — null", null)},

                {tokenCase("token — სხვა secret-ით ხელმოწერილი JWT", FOREIGN_SIGNED_JWT)},

                {tokenCase("token — JWT ხელმოწერის გარეშე", UNSIGNED_JWT)},
        };
    }
}
