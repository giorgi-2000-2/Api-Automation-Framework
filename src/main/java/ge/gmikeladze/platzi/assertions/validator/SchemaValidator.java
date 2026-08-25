package ge.gmikeladze.platzi.assertions.validator;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
@TestScoped
public class SchemaValidator extends Validator {
    private final SoftAssert softAssert;
@Inject
    public SchemaValidator(ITestReporter reporter, SoftAssert softAssert) {
        super(reporter);
        this.softAssert=softAssert;
    }

    public void verifySchema(Response response, String schemaPath) {
        try { response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
            reportPass("JSON სქემა შეესაბამება: " + schemaPath);}

        catch (AssertionError | RuntimeException e) {
            String failure = "JSON სქემა არ ემთხვევა " + schemaPath + "  " +e.getMessage();
            reportFail(failure);
            softAssert.fail(failure);
        }
    }

}
