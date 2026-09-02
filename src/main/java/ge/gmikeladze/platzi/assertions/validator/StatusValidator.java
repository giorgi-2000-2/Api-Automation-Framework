package ge.gmikeladze.platzi.assertions.validator;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.utils.config.IConfigForRequest;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;

@TestScoped
public class StatusValidator extends Validator {
    private final IConfigForRequest config;
    private final SoftAssert softAssert;

    @Inject
    public StatusValidator(ITestReporter reporter, IConfigForRequest config, SoftAssert softAssert) {
        super(reporter);
        this.config = config;
        this.softAssert = softAssert;
    }

    public void verifyStatus(Response response, HttpStatusCode expected) {
        int actual = response.statusCode();
        String message = "სტატუს კოდი: მოსალოდნელი " + expected.getCode() + " " + expected.getDescription() + ", მიღებული " + actual;

        if (actual == expected.getCode()) {
            reportPass(message);
        } else {
            String fullMessage = message + " " + bodyResponseMessage(response);
            reportFail(fullMessage);
            softAssert.fail(fullMessage);
        }
    }

    private String bodyResponseMessage(Response response) {
        String body = response.asString();
        if (body == null || body.isBlank()) {
            return "ცარიელი body";
        }

        int maxBodyLengthInMessage = config.maxBodyLengthInMessage();
        if (body.length() <= maxBodyLengthInMessage) {
            return body;
        }
        return body.substring(0, maxBodyLengthInMessage) + " შემოკლებულია";
    }
}