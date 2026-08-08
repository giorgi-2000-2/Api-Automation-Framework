package ge.gmikeladze.platzi.assertions;
import com.aventstack.extentreports.ExtentTest;
import ge.gmikeladze.platzi.di.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
import java.util.List;

@TestScoped
public class ResponseProductDtoAssert {
    private static final String NODE_NAME = "პროდუქტის ბიზნეს სცენარის შემოწმება";
    private GetResponseProductDto currentProduct;
    private Response responseProduct;
    private SoftAssert softAssert;
    private ExtentTest validationStep;

    public ResponseProductDtoAssert assertThat(GetResponseProductDto requestBody,SoftAssert softAssert) {
        this.currentProduct = requestBody;
        this.softAssert = softAssert;
        return this;
    }

    public ResponseProductDtoAssert assertThat(Response response,SoftAssert softAssert) {
        this.responseProduct = response;
        this.softAssert = softAssert;
        return this;
    }

    public void verifyBooleanResponseIsCorrect() {
        step("მიმდინარეობს წაშლის შემოწმება");
        softAssert.assertTrue(responseProduct.jsonPath().get(), "წაშლის შემოწმება");
    }

    public ResponseProductDtoAssert verifyTitleIsCorrect(String expectedTitle) {
        step("მიმდინარეობს სათაურის (Title) შემოწმება");
        softAssert.assertEquals(currentProduct.getTitle(), expectedTitle, "Title არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyPriceIsCorrect(Integer expectedPrice) {
        step("მიმდინარეობს ფასის (Price) შემოწმება");
        softAssert.assertEquals(currentProduct.getPrice(), expectedPrice, "Price არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyDescriptionIsCorrect(String expectedDescription) {
        step("მიმდინარეობს აღწერის (Description) შემოწმება");
        softAssert.assertEquals(currentProduct.getDescription(), expectedDescription, "Description არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyCategoryIdIsCorrect(Integer expectedCategoryId) {
        step("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
        softAssert.assertEquals(currentProduct.getCategory().getId(), expectedCategoryId, "Category ID არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyImagesAreCorrect(List<String> expectedImages) {
        step("მიმდინარეობს ფოტოების (Images) შემოწმება");
        softAssert.assertEquals(currentProduct.getImages(), expectedImages, "Images არასწორია");
        return this;
    }


    private void step(String message) {
        ExtentTest node = node();
        if (node != null) {
            node.info(message);
        } else {
            ExtentReportManager.info(message);
        }
    }

    private ExtentTest node() {
        if (validationStep == null) {
            validationStep = ExtentReportManager.createNode(NODE_NAME);
        }
        return validationStep;
    }
}
