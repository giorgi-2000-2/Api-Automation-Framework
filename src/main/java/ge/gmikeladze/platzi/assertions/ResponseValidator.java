package ge.gmikeladze.platzi.assertions;

import com.aventstack.extentreports.Status;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.di.TestScoped;
import ge.gmikeladze.platzi.utils.ConfigReader;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;

import static ge.gmikeladze.platzi.assertions.SchemaMapping.getPath;

@TestScoped
public class ResponseValidator {

    private static final int MAX_BODY_LENGTH_IN_MESSAGE = 1500;

    private final SoftAssert softAssert;

    @Inject
    public ResponseValidator(SoftAssert softAssert) {
        this.softAssert = softAssert;
    }

    public <T> T validate(Response response, HttpStatusCode expectedStatus, Class<T> dtoClass) {
         verifyResponseTime(response);
        verifyStatus(response, expectedStatus);


        verifyJsonContentType(response);


        verifySchema(response, getPath(dtoClass));

        return response.as(dtoClass);
    }

    public Response validateWithoutSchema(Response response, HttpStatusCode expected) {

        verifyStatus(response, expected);
        verifyResponseTime(response);
        return response;
    }

    public <T> List<T> validateList(Response response, HttpStatusCode expectedStatus,
                                    Class<T[]> arrayClass) {

        verifyStatus(response, expectedStatus);
        verifyJsonContentType(response);
        verifySchema(response, getPath(arrayClass));

        return Arrays.asList(response.as(arrayClass));
    }

    private void verifyStatus(Response response, HttpStatusCode expected) {
        int actual = response.statusCode();

        String message = "სტატუს კოდი: მოსალოდნელი [" + expected.getCode()
                + " " + expected.getDescription() + "], მიღებული [" + actual + "]";

        if (actual == expected.getCode()) {
            report(message + " — ✔");
            return;
        }

        String failure = message + " — ✘\nპასუხის სხეული: " + truncatedBody(response);
        reportFail(failure);
        Assert.fail(failure);
    }


    private void verifyJsonContentType(Response response) {
        String contentType = response.getContentType();

        if (contentType == null || contentType.trim().isEmpty()) {
            String failure = "Content-Type ჰედერი არ მოვიდა პასუხში";
            reportFail(failure);
            Assert.fail(failure);
        }

        if (!contentType.contains("application/json")) {
            String failure = "Content-Type არასწორია: მოსალოდნელი [application/json], მიღებული ["
                    + contentType + "]";
            reportFail(failure);
            Assert.fail(failure);
        }

        report("Content-Type: " + contentType + " — ✔");
    }

    private void verifySchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(
                    JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            report("JSON სქემა შეესაბამება: " + schemaPath + " — ✔");
        } catch (AssertionError schemaError) {
            String failure = "JSON სქემა არ ემთხვევა (" + schemaPath + ") — ✘\n"
                    + schemaError.getMessage();
            reportFail(failure);
            softAssert.fail(failure);
        }
    }

    private void verifyResponseTime(Response response) {
        long limit = ConfigReader.getInt("response.time");
        long actual = response.time();
        softAssert.assertTrue(actual < limit,
                "პასუხის დრო " + actual + "ms აჭარბებს ლიმიტს " + limit + "ms");
        report("პასუხის დრო: " + actual + "ms (ლიმიტი " + limit + "ms)");
    }

    private String truncatedBody(Response response) {
        String body = response.asString();
        if (body == null) {
            return "(ცარიელი)";
        }
        return body.length() <= MAX_BODY_LENGTH_IN_MESSAGE
                ? body
                : body.substring(0, MAX_BODY_LENGTH_IN_MESSAGE) + "... (შემოკლებულია)";
    }


    private void report(String message) {
        ExtentReportManager.log(Status.PASS, message);
    }

    private void reportFail(String message) {
        ExtentReportManager.log(Status.FAIL, message);
    }
}
