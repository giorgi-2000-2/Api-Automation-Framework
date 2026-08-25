package ge.gmikeladze.platzi.assertions.validator;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;

@TestScoped

public class ContentTypeValidator extends Validator {
    private static final String EXPECTED_CONTENT_TYPE = "application/json";
    private final SoftAssert softAssert;

    @Inject
    public ContentTypeValidator(ITestReporter reporter, SoftAssert softAssert) {
        super(reporter);
        this.softAssert = softAssert;
    }
    public void verify(Response response) {
        String contentType = response.getContentType();

        if (contentType == null || contentType.isBlank()) {
            String msg = "Content-Type header is missing";
            reportFail(msg);
            softAssert.fail(msg);
            return;
        }

        if (!contentType.toLowerCase().contains(EXPECTED_CONTENT_TYPE)) {
            String msg = "Content-Type is invalid. Expected to contain [" + EXPECTED_CONTENT_TYPE
                    + "], actual [" + contentType + "]";
            reportFail(msg);
            softAssert.fail(msg);
            return;
        }

        reportPass("Content-Type: " + contentType);
    }

}