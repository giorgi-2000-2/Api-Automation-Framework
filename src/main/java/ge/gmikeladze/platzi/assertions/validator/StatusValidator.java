package ge.gmikeladze.platzi.assertions.validator;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.utils.config.IConfigForRequest;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.response.Response;
import org.testng.Assert;

@TestScoped
public class StatusValidator extends Validator {
    private final IConfigForRequest config;

    @Inject
    public StatusValidator(ITestReporter reporter, IConfigForRequest config ) {
        super(reporter);
        this.config = config;
    }

    public void verifyStatus(Response response, HttpStatusCode expected) {
        int actual = response.statusCode();
        String message = "სტატუს კოდი: მოსალოდნელი " + expected.getCode() + " " + expected.getDescription() + ", მიღებული " + actual;

        if (actual == expected.getCode()) {
            reportPass(message);
        } else {
            String fullMessage = message + " " + bodyResponseMessage(response);
            reportFail(fullMessage);
            Assert.fail(fullMessage);
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