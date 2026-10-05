package ge.gmikeladze.platzi.tests;

import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.negative.AuthNegativeData;
import ge.gmikeladze.platzi.datafactories.negative.NegativeCase;
import ge.gmikeladze.platzi.dtos.request.AuthTokensDto;
import ge.gmikeladze.platzi.dtos.request.LoginRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import ge.gmikeladze.platzi.dtos.response.UnauthorizedErrorDto;
import ge.gmikeladze.platzi.dtos.response.error.ApiError;

import org.testng.annotations.Test;

import java.util.List;

public class AuthTest extends BaseApiTest {

    @Test(groups = {"smoke", "regression", "positive", "auth"})
    public void testLoginSuccessfully() {
        LoginRequestDto request = authData.validLogin();
        AuthTokensDto tokens = authSteps.get().login(request);

        authAssert.get().assertThat(tokens)
                .hasAccessToken()
                .hasRefreshToken()
                .accessTokenIsJwt()
                .refreshTokenIsJwt();
    }

    @Test(groups = {"smoke", "regression", "positive", "auth"})
    public void testGetProfileWithValidToken() {
        LoginRequestDto login = authData.validLogin();
        authSteps.get().login(login);

        GetUserResponseDto profile = authSteps.get().getProfile();

        userAssert.get().assertThat(profile)
                .hasEmail(login.getEmail());
    }

    @Test(groups = {"regression", "positive", "auth"})
    public void testRefreshTokenSuccessfully() {
        AuthTokensDto oldTokens = authSteps.get().login(authData.validLogin());
        AuthTokensDto newTokens = authSteps.get().refresh(oldTokens.getRefresh_token());

        authAssert.get().assertThat(newTokens)
                .hasAccessToken()
                .hasRefreshToken()
                .accessTokenIsJwt()
                .refreshTokenIsJwt();
    }


    @Test(groups = {"regression", "negative", "auth"},
            dataProvider = "invalidLogin", dataProviderClass = AuthNegativeData.class)
    public void testLoginNegative(NegativeCase<LoginRequestDto> testCase) {
        ApiError error = authSteps.get().loginExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression", "negative", "auth"},
            dataProvider = "invalidToken", dataProviderClass = AuthNegativeData.class)
    public void testGetProfileWithInvalidToken(NegativeCase<String> testCase) {
        ApiError error = authSteps.get().getProfileExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression", "negative", "auth"})
    public void testRefreshWithInvalidToken() {
        ApiError error = authSteps.get().refreshExpectingError(
                "invalid.refresh.token", HttpStatusCode.UNAUTHORIZED, UnauthorizedErrorDto.class);

        errorAssert.get().assertThat(error).messageIsNotBlank();
    }

    @Test(groups = {"regression", "negative", "auth"})
    public void testGetProfileWithoutAuthorizationHeader() {
        ApiError error = authSteps.get().getProfileWithoutAuth(
                HttpStatusCode.UNAUTHORIZED,
                UnauthorizedErrorDto.class);

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(List.of("Unauthorized"));
    }


}