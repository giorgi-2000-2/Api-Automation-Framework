package org.example.AssertionManager;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.example.DTOs.ResponseDto.GetResponseCategoryDTO;
import org.testng.Assert;
import java.util.List;
import static org.example.Utils.ExtentReportManager.getTest;

public class ResponseCategoryDtoAssert {
    private GetResponseCategoryDTO currentCategory;
    private Response responseCategory;

    ExtentTest validationStep = getTest().createNode(" კატეგორიის ბიზნეს სცენარის შემოწმება ");

    public ResponseCategoryDtoAssert assertThat(Response response) {
        this.responseCategory =response;
        return this;
    }


    public ResponseCategoryDtoAssert assertThat(GetResponseCategoryDTO requestBody) {
        this.currentCategory =requestBody;
        return this;
    }

    public ResponseCategoryDtoAssert verifyTitleIsCorrect(String expectedTitle) {
        validationStep.info(" მიმდინარეობს კატეგორიის Title-ის შემოწმება ");
        Assert.assertEquals(currentCategory.getName(), expectedTitle, " Title არასწორია ");
          return this;
    }

        public void verifyBooleanResponseIsCorrect() {
        getTest().info(" მიმდინარეობს წაშლის შემოწმება ");
           Assert.assertTrue(responseCategory.jsonPath().get(), " წაშლის შემოწმება ");

    }

    public ResponseCategoryDtoAssert verifyIdIsCorrect(int expectedId) {
          getTest().info("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
        Assert.assertEquals(currentCategory.getId(), expectedId, "Category ID არასწორია");
        return this;
    }

    public ResponseCategoryDtoAssert verifyNameIsCorrect(String expectedName) {
           getTest().info("მიმდინარეობს კატეგორიის სახელის შემოწმება");
        Assert.assertEquals(currentCategory.getName(), expectedName, "Category name არასწორია");
        return this;
    }

    public ResponseCategoryDtoAssert verifySlugIsCorrect(String expectedSlug) {
        getTest().info("მიმდინარეობს კატეგორიის Slug-ის შემოწმება");
          Assert.assertEquals(currentCategory.getSlug(), expectedSlug, "Category slug არასწორია");
        return this;
    }

    public ResponseCategoryDtoAssert verifyImageIsCorrect(String expectedImage) {
        validationStep.info("მიმდინარეობს კატეგორიის სურათის შემოწმება");
        Assert.assertEquals(currentCategory.getImage(), expectedImage, "Category image არასწორია");
          return this;
    }

    public ResponseCategoryDtoAssert verifyCreationAtIsCorrect(String expectedCreationAt) {
        getTest().info("მიმდინარეობს კატეგორიის შექმნის თარიღის შემოწმება");
        Assert.assertEquals(currentCategory.getCreationAt(), expectedCreationAt, "Category creationAt არასწორია");
            return this;
    }

    public ResponseCategoryDtoAssert verifyUpdatedAtIsCorrect(String expectedUpdatedAt) {
        getTest().info("მიმდინარეობს კატეგორიის განახლების თარიღის შემოწმება");
            Assert.assertEquals(currentCategory.getUpdatedAt(), expectedUpdatedAt, "Category updatedAt არასწორია");
        return this;
    }

    public void assertCategoryValidator(Response response) {
        List<GetResponseCategoryDTO> categories =
                response.jsonPath().getList("", GetResponseCategoryDTO.class);

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
