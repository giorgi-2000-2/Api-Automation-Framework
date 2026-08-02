package org.example.ApiService;
import io.restassured.response.Response;
import org.example.DTOs.ResponseDto.GetResponseCategoryDTO;
import org.example.Managers.ObjectManager;
import org.testng.Assert;
import java.util.List;
import static org.example.Utils.ExtentReportManager.getTest;

public class ApiAssertHelper {
    private ObjectManager api;

    public ApiAssertHelper(ObjectManager api) {
        this.api = api;
    }


    public <T> void assertEqualsWithLog(T actual, T expected, String description) {
        if (actual.equals(expected)) {
            getTest().pass(description + " — წარმატებულია");
        } else {
            getTest().fail(description + " — მოსალოდნელი: " + expected + ", მიღებული: " + actual);
        }

        Assert.assertEquals(actual, expected, description);
    }

    public <T> void assertNotEqualsWithLog(T actual, T expected, String description) {
        if (!actual.equals(expected)) {
            getTest().pass(description + " — წარმატებულია");
        } else {
            getTest().fail(description + " — მოსალოდნელი: " + expected + ", მიღებული: " + actual);
        }

        Assert.assertNotEquals(actual, expected, description);
    }

    public void assertTrue(boolean bool, String description){
        if (bool) {
            getTest().pass(description + " — წარმატებულია");
        } else {
            getTest().fail(description + " — მოსალოდნელი: " + " True " + ", მიღებული: " + " False ");
        }

        Assert.assertTrue(bool);
    }






    public void assertCategoryValidator(Response responseCategoryLimit) {
        List<GetResponseCategoryDTO> categories =
                responseCategoryLimit.jsonPath().getList("", GetResponseCategoryDTO.class);

        for (GetResponseCategoryDTO category : categories) {

           int id = category.getId();

            if (id > 0) {
                getTest().pass("კატეგორიის ID ვალიდურია: " + id);
            } else {
                getTest().fail("არავალიდური კატეგორიის ID: " + id);
            }

            Assert.assertTrue(id > 0, "ID უნდა იყოს 0-ზე მეტი! ნაპოვნია: " + id);
        }
    }



}