package org.example.AssertionManager;
import io.restassured.response.Response;
import org.example.ApiService.HttpStatusCode;
import org.example.Utils.ExtentReportManager;
import org.testng.Assert;

public class ResponseAssert {
    private Response response;


    public ResponseAssert assertThat(Response response) {
        this.response = response;
        return this;
    }

    public ResponseAssert time() {
        Assert.assertTrue( response.time()<20000, " არ არის 20000 ის ტოლი ");

        return this;
    }

    public ResponseAssert hasStatusCode(HttpStatusCode expectedStatus) {
        int actualCode = response.statusCode();
        int expectedCode = expectedStatus.getCode();
        String description = "სტატუს კოდის შემოწმება: მოსალოდნელი [" + expectedCode + "]";

        if (actualCode == expectedCode) {
            ExtentReportManager.getTest().pass(description + " — წარმატებულია");
        } else {
            ExtentReportManager.getTest().fail(description + " — მიღებული: [" + actualCode + "]");
        }

        Assert.assertEquals(actualCode, expectedCode, "Status Code Mismatch!");
        return this;
    }

    public ResponseAssert hasContentType(String expectedContentType) {
        String actualContentType = response.contentType();
        Assert.assertTrue(actualContentType.contains(expectedContentType), "Content-Type არ ემთხვევა!");
        ExtentReportManager.getTest().pass("Content-Type შემოწმება წარმატებულია: " + expectedContentType);

        return this;
    }
}