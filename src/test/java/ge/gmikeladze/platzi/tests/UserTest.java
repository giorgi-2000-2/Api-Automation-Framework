package ge.gmikeladze.platzi.tests;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.negative.NegativeCase;
import ge.gmikeladze.platzi.datafactories.negative.UserNegativeData;
import ge.gmikeladze.platzi.dtos.request.CreateUserDto;
import ge.gmikeladze.platzi.dtos.request.UpdateUserDto;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import ge.gmikeladze.platzi.dtos.response.error.ApiError;
import ge.gmikeladze.platzi.dtos.response.error.BadRequestResponse;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;


public class UserTest extends BaseApiTest {

    @Test(groups = {"smoke", "regression","positive"})
    public void createUserTest(){
        CreateUserDto createUserDto = userData.createUserWithData();
        GetUserResponseDto responseDto=  userSteps.get().create(createUserDto, HttpStatusCode.CREATED);
        userAssert.get().assertThat(responseDto)
                .hasEmail(createUserDto.getEmail())
                .hasName(createUserDto.getName())
                .hasRole(createUserDto.getRole())
                .hasCreationDatesPopulated();
    }
    @Test(groups = {"smoke", "regression", "positive"})
    public void createUserSuccessfully() {
        CreateUserDto request = userData.createUserWithData();
        GetUserResponseDto response = userSteps.get().create(request, HttpStatusCode.CREATED);

        userAssert.get().assertThat(response)
                .hasEmail(request.getEmail())
                .hasName(request.getName())
                .hasRole(request.getRole())
                .hasAvatar(request.getAvatar())
                .hasCreationDatesPopulated();
    }

    @Test(groups = {"smoke", "regression", "positive"})
    public void getUserByIdSuccessfully() {
        CreateUserDto request = userData.createUserWithData();
        GetUserResponseDto created = userSteps.get().create(request, HttpStatusCode.CREATED);

        GetUserResponseDto fetched = userSteps.get().getById(created.getId());

        userAssert.get().assertThat(fetched)
                .hasId(created.getId())
                .hasEmail(request.getEmail())
                .hasName(request.getName())
                .hasRole(request.getRole())
                .hasCreationDatesPopulated();
    }

    @Test(groups = {"smoke", "regression", "positive"})
    public void updateUserSuccessfully() {
        CreateUserDto createRequest = userData.createUserWithData();
        GetUserResponseDto created = userSteps.get().create(createRequest, HttpStatusCode.CREATED);

        UpdateUserDto updateRequest = userData.updateUserWithData();
        GetUserResponseDto updated = userSteps.get().update(created.getId(), updateRequest);

        userAssert.get().assertThat(updated)
                .hasId(created.getId())
                .hasEmail(updateRequest.getEmail())
                .hasName(updateRequest.getName())
                .hasRole(updateRequest.getRole())
                .hasAvatar(updateRequest.getAvatar())
                .hasCreationDatesPopulated();
    }

    @Test(groups = {"smoke", "regression", "positive"})
    public void deleteUserSuccessfully() {
        CreateUserDto request = userData.createUserWithData();
        GetUserResponseDto created = userSteps.get().create(request, HttpStatusCode.CREATED);

        Response deleteResponse = userSteps.get().delete(created.getId());
        userAssert.get().assertThat(deleteResponse).isDeletedSuccessfully();

        userSteps.get().getExpectingError(
                created.getId(),
                HttpStatusCode.BAD_REQUEST,
                BadRequestResponse.class
        );
    }
    @Test(groups = {"regression","negative"},
            dataProvider = "invalidUserCreate", dataProviderClass = UserNegativeData.class)

    public void testCreateUserNegative(NegativeCase<CreateUserDto> testCase) {
        ApiError error = userSteps.get().createExpectingError( testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());
        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());

    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidUserId", dataProviderClass = UserNegativeData.class)
    public void testGetUserByIdNegative(NegativeCase<Integer> testCase) {
        ApiError error = userSteps.get().getExpectingError( testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());
        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());

    }


    @Test(groups = {"regression","negative"},
            dataProvider = "invalidUserUpdate", dataProviderClass = UserNegativeData.class)
    public void testUpdateCategoryNegative(NegativeCase<CreateUserDto> testCase) {

        CreateUserDto createUserDto = userData.createUserWithData();
        GetUserResponseDto responseDto = userSteps.get().create(createUserDto, HttpStatusCode.CREATED);

        ApiError error = userSteps.get().updateExpectingError(responseDto.getId(),
                testCase.getPayload(),
                testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());

    }
    @Test(groups = {"regression", "negative"},
            dataProvider = "invalidUserDelete", dataProviderClass = UserNegativeData.class)
    public void testDeleteUserNegative(NegativeCase<Integer> testCase) {
        ApiError error = userSteps.get().deleteExpectingError(
                testCase.getPayload(),
                testCase.getExpectedStatus(),
                testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression", "negative"})
    public void testDeleteAlreadyDeletedUser() {
        CreateUserDto createRequest = userData.createUserWithData();
        GetUserResponseDto created = userSteps.get().create(createRequest, HttpStatusCode.CREATED);

        userSteps.get().delete(created.getId());
        ApiError error = userSteps.get().deleteExpectingError(
                created.getId(),
                HttpStatusCode.BAD_REQUEST,
                BadRequestResponse.class);

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(List.of("User"));
    }

}
