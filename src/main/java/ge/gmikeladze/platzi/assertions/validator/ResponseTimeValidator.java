package ge.gmikeladze.platzi.assertions.validator;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.utils.ConfigReader;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
@TestScoped
public class ResponseTimeValidator extends Validator {
    private final SoftAssert softAssert;

    @Inject
    public ResponseTimeValidator(ITestReporter reporter, SoftAssert softAssert) {
        super(reporter);
        this.softAssert = softAssert;
    }

    public void verifyResponseTime(Response response) {
        long limit = ConfigReader.getInt("response.time");
        long actual = response.time();

        if (actual < limit) {
            reportPass("პასუხის დრო: " + actual + "ms (ლიმიტი " + limit + "ms)");
        } else {
            String msg = "პასუხის დრო " + actual + "ms აჭარბებს ლიმიტს " + limit + "ms";
            reportFail(msg);
            softAssert.fail(msg);
        }
    }


}
