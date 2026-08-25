package ge.gmikeladze.platzi.assertions;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.assertions.validator.ContentTypeValidator;
import ge.gmikeladze.platzi.assertions.validator.ResponseTimeValidator;
import ge.gmikeladze.platzi.assertions.validator.SchemaValidator;
import ge.gmikeladze.platzi.assertions.validator.StatusValidator;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import static ge.gmikeladze.platzi.assertions.SchemaMapping.getPath;

@TestScoped
public class ResponseValidator {
    private final StatusValidator statusValidator;
    private final ContentTypeValidator contentTypeValidator;
    private final SchemaValidator schemaValidator;
    private final ResponseTimeValidator responseTimeValidator;

    @Inject
    public ResponseValidator(StatusValidator statusValidator,
                             ContentTypeValidator contentTypeValidator,
                             SchemaValidator schemaValidator,
                             ResponseTimeValidator responseTimeValidator) {
        this.statusValidator = statusValidator;
        this.contentTypeValidator = contentTypeValidator;
        this.schemaValidator = schemaValidator;
        this.responseTimeValidator = responseTimeValidator;
    }

    public <T> T validate(Response response, HttpStatusCode expectedStatus, Class<T> dtoClass) {
        responseTimeValidator.verifyResponseTime(response);
        statusValidator.verifyStatus(response, expectedStatus);
        contentTypeValidator.verify(response);
        schemaValidator.verifySchema(response, getPath(dtoClass));
        return response.as(dtoClass);
    }


    public <T> List<T> validateList(Response response, HttpStatusCode expectedStatus,
                                    Class<T[]> arrayClass) {
        responseTimeValidator.verifyResponseTime(response);
        statusValidator.verifyStatus(response, expectedStatus);
        contentTypeValidator.verify(response);
        schemaValidator.verifySchema(response, getPath(arrayClass));
        return Arrays.asList(response.as(arrayClass));
    }

    public Response validateWithoutSchema(Response response, HttpStatusCode expected) {
        responseTimeValidator.verifyResponseTime(response);
        statusValidator.verifyStatus(response, expected);
        return response;
    }

}
