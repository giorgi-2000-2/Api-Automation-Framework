package ge.gmikeladze.platzi.datafactories.negative;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.RandomDataFactory;
import ge.gmikeladze.platzi.dtos.request.CreateUserDto;
import ge.gmikeladze.platzi.dtos.request.UpdateUserDto;
import ge.gmikeladze.platzi.dtos.response.error.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.error.InternalServerErrorDto;
import ge.gmikeladze.platzi.dtos.response.error.PutBadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.error.ValidationErrorDto;
import org.testng.annotations.DataProvider;

import static ge.gmikeladze.platzi.datafactories.negative.NegativeCase.of;

@Singleton
public class UserNegativeData extends RandomDataFactory {
    private final RandomDataFactory randomDataFactory;
    @Inject
    public UserNegativeData(RandomDataFactory randomDataFactory) {
        this.randomDataFactory = randomDataFactory;
    }

    private CreateUserDto.CreateUserDtoBuilder validCreate() {
        return CreateUserDto.builder()
                .email(validEmail())
                .name(validName())
                .password(validPassword())
                .role("customer")
                .avatar(avatar());
    }

    private UpdateUserDto.UpdateUserDtoBuilder validUpdate() {
        return UpdateUserDto.builder()
                .email("updated" + validEmail())
                .name("updated" + validName())
                .password(validPassword())
                .role("admin")
                .avatar(avatar());
    }

    @DataProvider(name = "invalidUserCreate")
    public Object[][] invalidUserCreate() {
        return new Object[][]{

                {of("email — ცარიელი",
                        validCreate().email("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "email")},

                {of("email — null",
                        validCreate().email(null).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "email")},

                {of("email — არავალიდური ფორმატი (ტექსტი)",
                        validCreate().email("not-an-email").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "email")},

                {of("email — არასრული (მხოლოდ @)",
                        validCreate().email("@gmail.com").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "email")},

                {of("email — არასრული (domain-ის გარეშე)",
                        validCreate().email("user@").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "email")},

                {of("name — ცარიელი",
                        validCreate().name("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "name")},

                {of("name — null",
                        validCreate().name(null).build(),
                        HttpStatusCode.SERVER_ERROR, InternalServerErrorDto.class, "Internal")},

                {of("password — ცარიელი",
                        validCreate().password("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "password")},

                {of("password — null",
                        validCreate().password(null).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "password")},

                {of("avatar — არავალიდური URL",
                        validCreate().avatar("not-a-url").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "avatar")},

                {of("avatar — ცარიელი",
                        validCreate().avatar("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "avatar")},

                {of("avatar — არასრული URL",
                        validCreate().avatar("http://").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "avatar")},

                {of("avatar — null",
                        validCreate().avatar(null).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "avatar")},

                {of("role — ცარიელი",
                        validCreate().role("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "role")},

                {of("role — არარსებული მნიშვნელობა",
                        validCreate().role("superadmin").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "role")},

                {of("email და name — ორივე null",
                        CreateUserDto.builder()
                                .email(null)
                                .name(null)
                                .password(validPassword())
                                .role("customer")
                                .avatar(avatar())
                                .build(),
                        HttpStatusCode.SERVER_ERROR, InternalServerErrorDto.class, "Internal")},

                {of("ყველა სავალდებულო ველი null",
                        CreateUserDto.builder()
                                .email(null)
                                .name(null)
                                .password(null)
                                .role(null)
                                .avatar(null)
                                .build(),
                        HttpStatusCode.SERVER_ERROR, InternalServerErrorDto.class, "Internal")},
        };
    }


    @DataProvider(name = "invalidUserId")
    public Object[][] invalidUserId() {
        return new Object[][]{

                {of("id = 0", 0,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "User")},

                {of("id = -1", -1,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "User", "-1")},

                {of("id = Integer.MAX_VALUE (არარსებული)", Integer.MAX_VALUE,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "User", "2147483647")},
        };
    }






    @DataProvider(name = "invalidUserUpdate")
    public Object[][] invalidUserUpdate() {
        return new Object[][]{

                {of("update email — არავალიდური ფორმატი",
                        validUpdate().email("not-an-email").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "email")},

                {of("update email — ცარიელი",
                        validUpdate().email("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "email")},

                {of("update email — null",
                        validUpdate().email(null).build(),
                        HttpStatusCode.BAD_REQUEST, PutBadRequestResponse.class)},

                {of("update avatar — არავალიდური URL",
                        validUpdate().avatar("not-a-url").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "avatar")},

                {of("update avatar — ცარიელი",
                        validUpdate().avatar("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "avatar")},

                {of("update avatar — null",
                        validUpdate().avatar(null).build(),
                        HttpStatusCode.BAD_REQUEST, PutBadRequestResponse.class)},
        };
    }


    @DataProvider(name = "invalidUserDelete")
    public Object[][] invalidUserDelete() {
        return new Object[][]{

                {of("delete id = 0", 0,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "User")},

                {of("delete id = -1", -1,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "User", "-1")},

                {of("delete id = Integer.MAX_VALUE (არარსებული)", Integer.MAX_VALUE,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "User", "2147483647")},
        };
    }











}