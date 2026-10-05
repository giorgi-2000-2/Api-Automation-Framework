package ge.gmikeladze.platzi.assertions.assertsbusiness;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.dtos.request.AuthTokensDto;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import org.testng.asserts.SoftAssert;

@TestScoped
public class ResponseAuthAssert  extends BaseAssert<GetUserResponseDto, ResponseAuthAssert> {
  private AuthTokensDto actual;
    @Inject
    public ResponseAuthAssert(ITestReporter reporter, SoftAssert softAssert) {
        super(reporter,softAssert, "იუზერის ბიზნეს სცენარის შემოწმება");
    }

    public ResponseAuthAssert assertThat(AuthTokensDto actual) {
        this.actual = actual;
        return this;
    }
    public ResponseAuthAssert hasAccessToken() {
        softAssert.assertNotNull(actual.getAccess_token(), "access_token უნდა არსებობდეს");
        softAssert.assertFalse(actual.getAccess_token().isBlank(), "access_token არ უნდა იყოს ცარიელი");
        return this;
    }

    public ResponseAuthAssert hasRefreshToken() {
        softAssert.assertNotNull(actual.getRefresh_token(), "refresh_token უნდა არსებობდეს");
        softAssert.assertFalse(actual.getRefresh_token().isBlank(), "refresh_token არ უნდა იყოს ცარიელი");
        return this;
    }

    public ResponseAuthAssert accessTokenIsJwt() {
        softAssert.assertTrue(
                actual.getAccess_token() != null && actual.getAccess_token().startsWith("eyJ"),
                "access_token უნდა იყოს JWT ფორმატის"
        );
        return this;
    }

    public ResponseAuthAssert refreshTokenIsJwt() {
        AuthTokensDto tokens =  actual;
        softAssert.assertTrue(
                tokens.getRefresh_token() != null && tokens.getRefresh_token().startsWith("eyJ"),
                "refresh_token უნდა იყოს JWT ფორმატის"
        );
        return this;
    }


    public ResponseAuthAssert tokensAreNotNull() {
        return hasAccessToken().hasRefreshToken();
    }
}