package ge.gmikeladze.platzi.assertions;

import com.aventstack.extentreports.Status;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.annotations.TestScoped;
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
        verifyResponseTime(response);
        verifyStatus(response, expected);
        return response;
    }

    public <T> List<T> validateList(Response response, HttpStatusCode expectedStatus,
                                    Class<T[]> arrayClass) {
        verifyResponseTime(response);
        verifyStatus(response, expectedStatus);
        verifyJsonContentType(response);
        verifySchema(response, getPath(arrayClass));
        return Arrays.asList(response.as(arrayClass));
    }

    private void verifyStatus(Response response, HttpStatusCode expected) {
        int actual = response.statusCode();
        String message = "სტატუს კოდი: მოსალოდნელი [" + expected.getCode()
                + " " + expected.getDescription() + "], მიღებული [" + actual + "]";
            Assert.assertEquals(actual,expected.getCode(),message + "  " + bodyResponseMessage(response));

    }


    private boolean verifyJsonContentType(Response response) {
        String contentType = response.getContentType();
        if (contentType == null || contentType.isBlank()) {
            reportFail("Content-Type ჰედერი არ მოვიდა პასუხში");
            softAssert.fail("Content-Type ჰედერი არ მოვიდა პასუხში");
            return false;
        }
        if (!contentType.contains("application/json")) {
            String fail = "Content-Type არასწორია: მიღებული [" + contentType + "]";
            reportFail(fail); softAssert.fail(fail);
            return false;
        }
        reportPass("Content-Type: " + contentType);
        return true;
    }


    private void verifySchema(Response response, String schemaPath) {
        try {
            response.then().assertThat().body(
                    JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            reportPass("JSON სქემა შეესაბამება: " + schemaPath);
        } catch (AssertionError | RuntimeException e) {
            String failure = "JSON სქემა არ ემთხვევა " + schemaPath + "  " +e.getMessage();
            reportFail(failure);
            softAssert.fail(failure);
        }
    }

    private void verifyResponseTime(Response response) {
        long limit = ConfigReader.getInt("response.time");
        long actual = response.time();
        softAssert.assertTrue(actual < limit,
                "პასუხის დრო " + actual + "ms აჭარბებს ლიმიტს " + limit + "ms");
        reportPass("პასუხის დრო: " + actual + "ms (ლიმიტი " + limit + "ms)");
    }


    private String bodyResponseMessage(Response response) {
        String body = response.asString();

        if (body == null) {
            return "(ცარიელი)";
        }
        int maxBodyLengthInMessage = ConfigReader.getInt("maxBodyLengthInMessage");
        if (body.length() <= maxBodyLengthInMessage) {
            return body;
        } else {
            return body.substring(0, maxBodyLengthInMessage) + "... (შემოკლებულია)";
        }
    }


    private void reportPass(String message) {
        ExtentReportManager.log(Status.PASS, message);
    }

    private void reportFail(String message) {
        ExtentReportManager.log(Status.FAIL, message);
    }
}
