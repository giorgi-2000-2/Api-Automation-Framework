package org.example.assertionmanager;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.example.apiservice.HttpStatusCode;
import org.example.utils.ConfigReader;
import org.example.utils.ExtentReportManager;
import org.testng.asserts.SoftAssert;
import java.util.Arrays;
import java.util.List;
import static org.example.assertionmanager.SchemaMapping.getPath;



public class ResponseValidator {
    private SoftAssert softAssert;
    public ResponseValidator(SoftAssert softAssert){
        this.softAssert=softAssert;
    }

    public <T> T validate(Response response, HttpStatusCode expectedStatus, Class<T> dtoClass) {
        softAssert.assertEquals(response.getStatusCode(), expectedStatus.getCode(), "სტატუს კოდი არ ემთხვევა!");
        softAssert.assertTrue(response.getContentType().contains("application/json"), "Content-Type არასწორია!");

        String schemaPath = getPath(dtoClass);

            response.then().assertThat().body(
                    JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath)
            );

        return response.as(dtoClass);



    }
    public Response validateWithoutSchema(Response response, HttpStatusCode expected) {
        verifyStatus(response, expected);
        verifyResponseTime(response);
        return response;
    }

    private void verifyResponseTime(Response response) {
        long limit = ConfigReader.getInt("response.time");
        long actual = response.time();
        softAssert.assertTrue(actual < limit,
                "პასუხის დრო " + actual + "ms აჭარბებს ლიმიტს " + limit + "ms");
        report("პასუხის დრო: " + actual + "ms (ლიმიტი " + limit + "ms)");
    }



    private void verifyStatus(Response response, HttpStatusCode expected) {
        int actual = response.statusCode();
        String message = "სტატუს კოდი: მოსალოდნელი [" + expected.getCode()
                + " " + expected.getDescription() + "], მიღებული [" + actual + "]";

        if (actual == expected.getCode()) {
            report(message + " — ✔");
        } else {
            reportFail(message + " — ✘\nსხეული: " + response.asString());
        }
        softAssert.assertEquals(actual, expected.getCode(), "Status Code Mismatch!");
    }


    public <T> List<T> validateList(Response response, HttpStatusCode expectedStatus,
                                    Class<T[]> arrayClass) {

        softAssert.assertEquals(response.getStatusCode(), expectedStatus.getCode(),
                "სტატუს კოდი არ ემთხვევა! სხეული: " + response.asString());

        softAssert.assertTrue(response.getContentType() != null
                        && response.getContentType().contains("application/json"),
                "Content-Type არასწორია: " + response.getContentType());

        response.then().assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(getPath(arrayClass)));

        return Arrays.asList(response.as(arrayClass));
    }



    private void report(String message) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().pass(message);
        }
    }

    private void reportFail(String message) {
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().fail(message);
        }
    }

}