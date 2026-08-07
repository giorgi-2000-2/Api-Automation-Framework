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

/**
 * პასუხის ვალიდაციის ცენტრალური წერტილი.
 *
 * FIX C-1: assertion-ების თანმიმდევრობა და სიმკაცრე გადაწერილია.
 *
 *   იყო:  სტატუსი — SOFT, Content-Type — SOFT, სქემა — HARD (მაშინვე აგდებდა).
 *         ამის გამო არასწორი სტატუსის დროსაც კი ფრეიმვორკი აგრძელებდა და ცდილობდა
 *         წარმატებული (მაგ. 201) body-ის შედარებას error-სქემასთან. შედეგად ტესტი ეცემოდა
 *         სრულიად უაზრო შეტყობინებით ("object instance has properties which are not allowed..."),
 *         ხოლო ნამდვილი მიზეზი — "მოსალოდნელი 400, მიღებული 201" — soft იყო და იკარგებოდა.
 *
 *   არის: სტატუსი — HARD, fail-fast (preconditional assertion — თუ ის არასწორია, დანარჩენი უაზროა);
 *         Content-Type — HARD და null-safe;
 *         სქემა — SOFT (დამატებითი ინფორმაცია, ერთ გაშვებაზე ყველა კონტრაქტის დარღვევას ვხედავთ).
 *
 * FIX C-6: Content-Type-ის შემოწმება ახლა null-safe-ია validate()-შიც (ადრე მხოლოდ validateList()-ში იყო).
 */
@TestScoped
public class ResponseValidator {

    /** რამდენი სიმბოლო მოხვდეს შეცდომის ტექსტში პასუხის სხეულიდან. */
    private static final int MAX_BODY_LENGTH_IN_MESSAGE = 1500;

    private final SoftAssert softAssert;

    @Inject
    public ResponseValidator(SoftAssert softAssert) {
        this.softAssert = softAssert;
    }

    public <T> T validate(Response response, HttpStatusCode expectedStatus, Class<T> dtoClass) {
        // 1. სტატუსი — HARD. არასწორ სტატუსზე მაშინვე ვჩერდებით, სრული დიაგნოსტიკით.
        verifyStatus(response, expectedStatus);

        // 2. Content-Type — HARD და null-safe.
        verifyJsonContentType(response);

        // 3. სქემა — SOFT. კონტრაქტის დარღვევა ტესტს ჩააგდებს, მაგრამ არა assertion-ების შუაში.
        verifySchema(response, getPath(dtoClass));

        return response.as(dtoClass);
    }

    public Response validateWithoutSchema(Response response, HttpStatusCode expected) {
        // FIX C-1: სტატუსის შემოწმება აქაც იმავე hard fail-fast ლოგიკას იყენებს,
        //          რომ ორ გზას შორის ქცევა არ განსხვავდებოდეს.
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

    /**
     * FIX C-1: სტატუს-კოდი ახლა HARD assertion-ია.
     * შეცდომის ტექსტში შედის მოსალოდნელი კოდი, მიღებული კოდი და პასუხის სხეული —
     * ანუ ერთი შეხედვით ჩანს რა მოხდა, დამატებითი გაშვების გარეშე.
     */
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

    /**
     * FIX C-6: null-safe Content-Type შემოწმება.
     * ადრე validate()-ში ეწერა response.getContentType().contains(...) — ცარიელ ან
     * header-ის გარეშე პასუხზე ეს NullPointerException-ს იძლეოდა.
     */
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

    /**
     * FIX C-1: სქემის ვალიდაცია SOFT გახდა.
     * JsonSchemaValidator AssertionError-ს აგდებს, ამიტომ ვიჭერთ და SoftAssert-ში ვწერთ —
     * ასე ერთ გაშვებაზე ვხედავთ კონტრაქტის ყველა დარღვევას და არა მხოლოდ პირველს.
     */
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

    /** გრძელი პასუხის სხეული ისე იჭრება, რომ შეცდომის ტექსტი წასაკითხი დარჩეს. */
    private String truncatedBody(Response response) {
        String body = response.asString();
        if (body == null) {
            return "(ცარიელი)";
        }
        return body.length() <= MAX_BODY_LENGTH_IN_MESSAGE
                ? body
                : body.substring(0, MAX_BODY_LENGTH_IN_MESSAGE) + "... (შემოკლებულია)";
    }

    // FIX C-5: რეპორტში ჩაწერა ახლა null-safe wrapper-ით ხდება.
    private void report(String message) {
        ExtentReportManager.log(Status.PASS, message);
    }

    private void reportFail(String message) {
        ExtentReportManager.log(Status.FAIL, message);
    }
}
