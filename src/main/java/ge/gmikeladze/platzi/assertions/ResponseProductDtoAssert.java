package ge.gmikeladze.platzi.assertions;

import com.aventstack.extentreports.ExtentTest;
import ge.gmikeladze.platzi.di.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;

/**
 * პროდუქტის ბიზნეს-ვალიდაციების fluent DSL.
 *
 * FIX C-5: კვანძი აღარ იქმნება ველის ინიციალიზატორში (getTest().createNode(...)),
 *          რაც NullPointerException-ს იწვევდა ობიექტის შექმნისთანავე, თუ ExtentTest ჯერ არ არსებობდა,
 *          და რეპორტში ტოვებდა ცარიელ კვანძებს. ახლა კვანძი lazy-ად და null-safe-ად იქმნება.
 */
@TestScoped
public class ResponseProductDtoAssert {

    private static final String NODE_NAME = "პროდუქტის ბიზნეს სცენარის შემოწმება";

    private GetResponseProductDto currentProduct;
    private Response responseProduct;

    /** FIX C-5: აღარ არის ველის ინიციალიზატორი — კვანძი lazy-ად იქმნება. */
    private ExtentTest validationStep;

    public ResponseProductDtoAssert assertThat(GetResponseProductDto requestBody) {
        this.currentProduct = requestBody;
        return this;
    }

    public ResponseProductDtoAssert assertThat(Response response) {
        this.responseProduct = response;
        return this;
    }

    public void verifyBooleanResponseIsCorrect() {
        step("მიმდინარეობს წაშლის შემოწმება");
        Assert.assertTrue(responseProduct.jsonPath().get(), "წაშლის შემოწმება");
    }

    public ResponseProductDtoAssert verifyTitleIsCorrect(String expectedTitle) {
        step("მიმდინარეობს სათაურის (Title) შემოწმება");
        Assert.assertEquals(currentProduct.getTitle(), expectedTitle, "Title არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyPriceIsCorrect(Integer expectedPrice) {
        step("მიმდინარეობს ფასის (Price) შემოწმება");
        Assert.assertEquals(currentProduct.getPrice(), expectedPrice, "Price არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyDescriptionIsCorrect(String expectedDescription) {
        step("მიმდინარეობს აღწერის (Description) შემოწმება");
        Assert.assertEquals(currentProduct.getDescription(), expectedDescription, "Description არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyCategoryIdIsCorrect(Integer expectedCategoryId) {
        step("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
        Assert.assertEquals(currentProduct.getCategory().getId(), expectedCategoryId, "Category ID არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyImagesAreCorrect(List<String> expectedImages) {
        step("მიმდინარეობს ფოტოების (Images) შემოწმება");
        Assert.assertEquals(currentProduct.getImages(), expectedImages, "Images არასწორია");
        return this;
    }

    /**
     * FIX C-5: lazy და null-safe კვანძის შექმნა.
     */
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
