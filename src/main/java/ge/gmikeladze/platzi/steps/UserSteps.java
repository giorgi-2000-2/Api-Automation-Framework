package ge.gmikeladze.platzi.steps;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiclient.ApiEndpoint;
import ge.gmikeladze.platzi.apiclient.GenericClient;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.cleanup.ResourceKey;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.request.CreateUserDto;
import ge.gmikeladze.platzi.dtos.request.UpdateUserDto;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import ge.gmikeladze.platzi.utils.ITestReporter;
import io.restassured.response.Response;


@TestScoped
public class UserSteps  extends AbstractResourceSteps<CreateUserDto, GetUserResponseDto, UpdateUserDto> {
    @Inject
    public UserSteps(GenericClient genericClient,
                     ResponseValidator validator, ITestReporter reporter, TestContext testContext) {
        super(genericClient, validator,reporter, testContext);
    }



    @Override
    protected ApiEndpoint collectionEndpoint() {
        return ApiEndpoint.USER;
    }

    @Override
    protected ApiEndpoint itemEndpoint() {
        return ApiEndpoint.USER_ID;
    }

    @Override
    protected Class<GetUserResponseDto> responseType() {
        return GetUserResponseDto.class;
    }

    @Override
    protected String resourceType() {
        return ResourceKey.TYPE_USER;
    }

    @Override
    protected void bestEffortDelete(int id) {
        Response response = genericClient.delete(ApiEndpoint.USER_ID, id);
        if (response.statusCode() != HttpStatusCode.OK.getCode()) {
            logBestEffortFailure(id, response.statusCode());
        }
    }



}
















