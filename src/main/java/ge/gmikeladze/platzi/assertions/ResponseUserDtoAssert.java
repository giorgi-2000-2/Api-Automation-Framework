package ge.gmikeladze.platzi.assertions;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

@TestScoped
public class ResponseUserDtoAssert extends BaseAssert {
    private GetUserResponseDto userResponseDto;
    private Response rawResponse;

    @Inject
    ResponseUserDtoAssert(SoftAssert softAssert) {
        super(softAssert, "იუზერის ბიზნეს სცენარის შემოწმება");
    }


    public ResponseUserDtoAssert assertThat(GetUserResponseDto userResponseDto) {
        Assert.assertNotNull(userResponseDto, "User DTO არ უნდა იყოს null");
        this.userResponseDto = userResponseDto;
        return this;
    }

    public ResponseUserDtoAssert assertThat(Response response) {
        Assert.assertNotNull(response, "API Response არ უნდა იყოს null");
        this.rawResponse = response;
        return this;
    }
    public ResponseUserDtoAssert hasId(Integer expectedId) {
        step("ID-ის შემოწმება");
        softAssert.assertEquals(userResponseDto.getId(), expectedId, "ID არასწორია");
        return this;
    }

    public ResponseUserDtoAssert hasEmail(String expectedEmail) {
        step("შემოწმება: ელ. ფოსტა (Email)");
        softAssert.assertEquals(userResponseDto.getEmail(), expectedEmail, "Email არასწორია");
        return this;
    }

    public ResponseUserDtoAssert hasName(String expectedName) {
        step("შემოწმება: სახელი (Name)");
        softAssert.assertEquals(userResponseDto.getName(), expectedName, "მომხმარებლის სახელი არასწორია");
        return this;
    }

    public ResponseUserDtoAssert hasRole(String expectedRole) {
        step("შემოწმება: როლი (Role)");
        softAssert.assertEquals(userResponseDto.getRole(), expectedRole, "მომხმარებლის როლი არასწორია");
        return this;
    }

    public ResponseUserDtoAssert hasAvatar(String expectedAvatarUrl) {
        step("შემოწმება: ავატარის ბმული (Avatar)");
        Assert.assertNotNull(userResponseDto.getAvatar(), "ავატარის ბმული არ უნდა იყოს Null");
        softAssert.assertEquals(userResponseDto.getAvatar(), expectedAvatarUrl, "ავატარის ბმული არასწორია");
        return this;
    }
    public void verifyBooleanResponseIsCorrect() {
        step("მიმდინარეობს წაშლის შემოწმება");
        Object boolResponse = rawResponse.jsonPath().get();
        softAssert.assertEquals(boolResponse, Boolean.TRUE, "წაშლის პასუხი უნდა იყოს true, მიღებულია: " + boolResponse);
    }

    public ResponseUserDtoAssert hasCreationDatesPopulated() {
        step("შემოწმება: შექმნისა და განახლების თარიღები (creationAt / updatedAt)");
        softAssert.assertNotNull(userResponseDto.getCreationAt(), "creationAt არ უნდა იყოს ცარიელი");
        softAssert.assertNotNull(userResponseDto.getUpdatedAt(), "updatedAt არ უნდა იყოს ცარიელი");
        return this;
    }

    public void isDeletedSuccessfully() {
        step("შემოწმება: წაშლის სტატუსი (True/False)");

        Assert.assertNotNull(rawResponse, "Response ობიექტი ცარიელია, წაშლის სტატუსის შემოწმება შეუძლებელია");

        Object boolResponse = rawResponse.jsonPath().get();
        softAssert.assertEquals(boolResponse, Boolean.TRUE,
                "წაშლის პასუხი უნდა იყოს true, თუმცა დაბრუნდა: " + boolResponse);
    }
}