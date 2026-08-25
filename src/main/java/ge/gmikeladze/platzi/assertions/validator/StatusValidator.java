package ge.gmikeladze.platzi.assertions.validator;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.utils.ConfigReader;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;

@TestScoped
public class StatusValidator extends Validator {

    private final SoftAssert softAssert;

    @Inject
    public StatusValidator(ITestReporter reporter, SoftAssert softAssert) {
        super(reporter);
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

        int maxBodyLengthInMessage = ConfigReader.getInt("max.body.length.in.message");
        if (body.length() <= maxBodyLengthInMessage) {
            return body;
        }
        return body.substring(0, maxBodyLengthInMessage) + " შემოკლებულია";
    }
}