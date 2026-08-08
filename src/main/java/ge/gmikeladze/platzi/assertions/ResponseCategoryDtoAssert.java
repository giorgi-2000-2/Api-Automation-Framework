package ge.gmikeladze.platzi.assertions;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import ge.gmikeladze.platzi.di.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
import java.util.List;
import java.util.Optional;

@TestScoped
public class ResponseCategoryDtoAssert {
    private static final String NODE_NAME = "კატეგორიის ბიზნეს სცენარის შემოწმება";
    private GetResponseCategoryDto currentCategory;
    private List<GetResponseCategoryDto> responseCategoryDtos;
    private Response responseProduct;
    private ExtentTest validationStep;
    private SoftAssert softAssert;

    public ResponseCategoryDtoAssert assertThat(Response response,SoftAssert softAssert) {
        this.responseProduct = response;
        this.softAssert = softAssert;
        return this;
    }

    public ResponseCategoryDtoAssert assertThat(GetResponseCategoryDto requestBody,SoftAssert softAssert) {
        this.currentCategory = requestBody;
        this.softAssert = softAssert;
        return this;
    }

    public ResponseCategoryDtoAssert assertThat(List<GetResponseCategoryDto> responseCategoryDtos,SoftAssert softAssert) {
        this.responseCategoryDtos = responseCategoryDtos;
        this.softAssert = softAssert;
        return this;
    }

    public ResponseCategoryDtoAssert verifyTitleIsCorrect(String expectedTitle) {
        step("მიმდინარეობს კატეგორიის Title-ის შემოწმება");
        softAssert.assertEquals(currentCategory.getName(), expectedTitle, "Title არასწორია");
        return this;
    }

    public ResponseCategoryDtoAssert verifyIdIsCorrect(int expectedId) {

        step("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
        softAssert.assertEquals(Optional.ofNullable(currentCategory.getId()), expectedId, "Category ID არასწორია");
        return this;
    }

    public ResponseCategoryDtoAssert verifyImageIsCorrect(String expectedImage) {
        step("მიმდინარეობს კატეგორიის სურათის შემოწმება");
        softAssert.assertEquals(currentCategory.getImage(), expectedImage, "Category image არასწორია");
        return this;
    }

    public void verifyBooleanResponseIsCorrect() {
        step("მიმდინარეობს წაშლის შემოწმება");
        softAssert.assertTrue(responseProduct.jsonPath().get(), "წაშლის შემოწმება");
    }

    public void assertCategoryValidator() {
        for (GetResponseCategoryDto category : responseCategoryDtos) {

            int id = category.getId();

            if (id > 0) {
                log(Status.PASS, "კატეგორიის ID ვალიდურია: " + id);
            } else {
                log(Status.FAIL, "არავალიდური კატეგორიის ID: " + id);
            }

            softAssert.assertTrue(id > 0, "ID უნდა იყოს 0-ზე მეტი! ნაპოვნია: " + id);
        }
    }


    private void step(String message) {
        ExtentTest node = node();
        if (node != null) {
            node.info(message);
        } else {
            ExtentReportManager.info(message);
        }
    }

    private void log(Status status, String message) {
        ExtentTest node = node();
        if (node != null) {
            node.log(status, message);
        } else {
            ExtentReportManager.log(status, message);
        }
    }

    private ExtentTest node() {
        if (validationStep == null) {
            validationStep = ExtentReportManager.createNode(NODE_NAME);
        }
        return validationStep;
    }
}
