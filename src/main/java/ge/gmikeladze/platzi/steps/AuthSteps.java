package ge.gmikeladze.platzi.steps;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiclient.ApiEndpoint;
import ge.gmikeladze.platzi.apiclient.GenericClient;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.request.AuthTokensDto;
import ge.gmikeladze.platzi.dtos.request.LoginRequestDto;
import ge.gmikeladze.platzi.dtos.request.RefreshTokenRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;

@TestScoped
public class AuthSteps extends BaseSteps {

    private final GenericClient genericClient;
    private final TestContext testContext;

    @Inject
    public AuthSteps(GenericClient genericClient,
                     ResponseValidator validator,
                     ITestReporter reporter,
                     TestContext testContext) {
        super(reporter, validator);
        this.genericClient = genericClient;
        this.testContext = testContext;
    }



    public AuthTokensDto login(LoginRequestDto body) {
        step("Login: " + body.getEmail());
        AuthTokensDto tokens = validator.validate(
                genericClient.post(ApiEndpoint.AUTH_LOGIN, body),
                HttpStatusCode.CREATED,
                AuthTokensDto.class
        );
        testContext.setAccessToken(tokens.getAccess_token());
        testContext.setRefreshToken(tokens.getRefresh_token());
        return tokens;
    }

    public <T> T loginExpectingError(LoginRequestDto body,
                                     HttpStatusCode expectedStatus,
                                     Class<T> errorDto) {
        step("Login expecting error: " + body.getEmail());
        return validator.validate(
                genericClient.post(ApiEndpoint.AUTH_LOGIN, body),
                expectedStatus,
                errorDto
        );
    }


    public GetUserResponseDto getProfile() {
        step("Get authenticated profile");
        return validator.validate(
                genericClient.getWithAuth(ApiEndpoint.AUTH_PROFILE, testContext.getAccessToken()),
                HttpStatusCode.OK,
                GetUserResponseDto.class
        );
    }

    public <T> T getProfileExpectingError(String token,
                                          HttpStatusCode expectedStatus,
                                          Class<T> errorDto) {
        step("Get profile expecting error");
        return validator.validate(
                genericClient.getWithAuth(ApiEndpoint.AUTH_PROFILE, token),
                expectedStatus,
                errorDto
        );
    }


    public AuthTokensDto refresh(String refreshToken) {
        step("Refresh token");
        RefreshTokenRequestDto body = RefreshTokenRequestDto.builder()
                .refreshToken(refreshToken)
                .build();

        AuthTokensDto tokens = validator.validate(
                genericClient.post(ApiEndpoint.AUTH_REFRESH, body),
                HttpStatusCode.CREATED,
                AuthTokensDto.class
        );
        testContext.setAccessToken(tokens.getAccess_token());
        testContext.setRefreshToken(tokens.getRefresh_token());
        return tokens;
    }

    public <T> T refreshExpectingError(String refreshToken,
                                       HttpStatusCode expectedStatus,
                                       Class<T> errorDto) {
        step("Refresh token expecting error");
        RefreshTokenRequestDto body = RefreshTokenRequestDto.builder()
                .refreshToken(refreshToken)
                .build();

        return validator.validate(
                genericClient.post(ApiEndpoint.AUTH_REFRESH, body),
                expectedStatus,
                errorDto
        );
    }

    public <T> T getProfileWithoutAuth(HttpStatusCode expectedStatus, Class<T> errorDto) {
        step("Get profile without Authorization header");
        return validator.validate(
                genericClient.getWithoutAuth(ApiEndpoint.AUTH_PROFILE),
                expectedStatus,
                errorDto
        );
    }
}