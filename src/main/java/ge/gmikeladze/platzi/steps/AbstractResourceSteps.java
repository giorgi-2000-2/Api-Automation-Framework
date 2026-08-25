package ge.gmikeladze.platzi.steps;

import ge.gmikeladze.platzi.apiclient.ApiEndpoint;
import ge.gmikeladze.platzi.apiclient.GenericClient;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.cleanup.ResourceKey;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.response.Identifiable;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.response.Response;

import java.util.Map;

public abstract class AbstractResourceSteps<TRequest, TResponse extends Identifiable, TUpdate> extends BaseSteps implements IResourceSteps<TRequest, TResponse, TUpdate> {
    protected final TestContext testContext;
    protected final GenericClient genericClient;
    protected AbstractResourceSteps(GenericClient genericClient,
                                    ResponseValidator validator, ITestReporter reporter, TestContext testContext) {
        super(reporter,validator);
        this.genericClient = genericClient;
        this.testContext = testContext;
    }


    protected abstract ApiEndpoint collectionEndpoint();

    protected abstract ApiEndpoint itemEndpoint();

    protected abstract Class<TResponse> responseType();

    protected abstract String resourceType();

    protected abstract void bestEffortDelete(int id);

    @Override
    public TResponse create(TRequest body) {
        return create(body, HttpStatusCode.CREATED);
    }

    @Override
    public TResponse create(TRequest body, HttpStatusCode expectedStatus) {
        step("რესურსის შექმნა " + resourceType());
        Response response = genericClient.create(collectionEndpoint(), body);
        registerIfCreated(response);
        return validator.validate(response, expectedStatus, responseType());
    }



    @Override
    public TResponse getById(int id) {
        step("რესურსის წამოღება id=" + id + " " + resourceType());
        return validator.validate(
                genericClient.getByPath(itemEndpoint(), Map.of("id", id)),
                HttpStatusCode.OK,
                responseType()
        );
    }

    @Override
    public TResponse update(int id, TUpdate body) {
        step("რესურსის განახლება id=" + id + " " + resourceType());
        return validator.validate(
                genericClient.update(itemEndpoint(), id, body),
                HttpStatusCode.OK,
                responseType()
        );
    }

    @Override
    public Response delete(int id) {
        step("რესურსის წაშლა id=" + id + " " + resourceType());
        Response response = validator.validateWithoutSchema(
                genericClient.delete(itemEndpoint(), id),
                HttpStatusCode.OK
        );
        testContext.getCleanupRegistry().markCompleted(
                new ResourceKey(resourceType(), id)
        );
        return response;
    }

    @Override
    public <T> T createExpectingError(TRequest body,
                                      HttpStatusCode expectedStatus,
                                      Class<T> errorDto) {
        step("რესურსის შექმნის მცდელობა არავალიდური მონაცემებით " + resourceType());
        Response response = genericClient.create(collectionEndpoint(), body);
        registerIfCreated(response);
        return validator.validate(response, expectedStatus, errorDto);
    }

    @Override
    public <T> T getExpectingError(int id,
                                   HttpStatusCode expectedStatus,
                                   Class<T> errorDto) {
        step("რესურსის წამოღების მცდელობა არავალიდური id-ით: " + id);
        return validator.validate(
                genericClient.getByPath(itemEndpoint(), Map.of("id", id)),
                expectedStatus,
                errorDto
        );
    }

    @Override
    public <T> T updateExpectingError(int id,
                                      Object body,
                                      HttpStatusCode expectedStatus,
                                      Class<T> errorDto) {
        step("რესურსის განახლების მცდელობა არავალიდური მონაცემებით, id=" + id);
        return validator.validate(
                genericClient.update(itemEndpoint(), id, body),
                expectedStatus,
                errorDto
        );
    }

    @Override
    public <T> T deleteExpectingError(int id,
                                      HttpStatusCode expectedStatus,
                                      Class<T> errorDto) {
        step("რესურსის წაშლის მცდელობა არავალიდური id-ით: " + id);
        return validator.validate(
                genericClient.delete(itemEndpoint(), id),
                expectedStatus,
                errorDto
        );
    }

    protected void logBestEffortFailure(int id, int statusCode) {
        reporter.info("cleanup: " + resourceType() + " " + id + " ვერ წაიშალა სტატუსი " + statusCode);
    }


    protected void registerIfCreated(Response response) {
        if (response == null) return;

        try {
            Integer id = response.jsonPath().get("id");
            if (id != null && id > 0) {

                testContext.getCleanupRegistry().register(new ResourceKey(resourceType(), id), () -> bestEffortDelete(id));

                reporter.info("Cleanup-ზე დარეგისტრირდა " + resourceType() + " " + id);
            }
        } catch (Exception e) {
            reporter.info("Cleanup რეგისტრაცია ვერ მოხერხდა " + resourceType() + " : " + e.getMessage());

        }
    }
}
