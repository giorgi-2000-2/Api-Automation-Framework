package ge.gmikeladze.platzi.assertions.assertsbusiness;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import org.testng.asserts.SoftAssert;

@TestScoped
public class ResponseUserAssert extends BaseAssert<GetUserResponseDto, ResponseUserAssert> {

    @Inject
    public ResponseUserAssert(SoftAssert softAssert) {
        super(softAssert, "იუზერის ბიზნეს სცენარის შემოწმება");
    }

    public ResponseUserAssert hasId(Integer expectedId) {
        return hasField(GetUserResponseDto::getId, expectedId, "ID");
    }

    public ResponseUserAssert hasEmail(String expectedEmail) {
        return hasField(GetUserResponseDto::getEmail, expectedEmail, "Email");
    }

    public ResponseUserAssert hasName(String expectedName) {
        return hasField(GetUserResponseDto::getName, expectedName, "Name");
    }

    public ResponseUserAssert hasRole(String expectedRole) {
        return hasField(GetUserResponseDto::getRole, expectedRole, "Role");
    }
public ResponseUserAssert hasAvatar(String expectedAvatar){
        return hasField(GetUserResponseDto::getAvatar,expectedAvatar,"Avatar");
}

    public void hasCreationDatesPopulated() {
        hasNotNullFields(
                "შემოწმება: შექმნისა და განახლების თარიღები (creationAt / updatedAt)",
                GetUserResponseDto::getCreationAt,
                GetUserResponseDto::getUpdatedAt
        );
    }



}