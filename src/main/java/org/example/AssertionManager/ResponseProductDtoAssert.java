package org.example.AssertionManager;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.example.DTOs.ResponseDto.GetResponseProductDto;
import org.testng.Assert;
import java.util.List;
import static org.example.Utils.ExtentReportManager.getTest;

public class ResponseProductDtoAssert {
    private GetResponseProductDto currentProduct;
    private Response responseProduct;
    ExtentTest validationStep = getTest().createNode(" პროდუქტის ბიზნეს სცენარის შემოწმება ");

    public ResponseProductDtoAssert assertThat(GetResponseProductDto requestBody) {
        this.currentProduct =requestBody;
        return this;
    }



    public ResponseProductDtoAssert assertThat(Response response) {
        this.responseProduct =response;
        return this;
    }


    public void verifyBooleanResponseIsCorrect() {
        validationStep.info(" მიმდინარეობს წაშლის შემოწმება ");
        Assert.assertTrue(responseProduct.jsonPath().get(), " წაშლის შემოწმება ");

    }

    public ResponseProductDtoAssert verifyTitleIsCorrect(String expectedTitle) {
        validationStep.info("მიმდინარეობს სათაურის (Title) შემოწმება");
        Assert.assertEquals(currentProduct.getTitle(), expectedTitle, "Title არასწორია");
        return this;
    }


    public ResponseProductDtoAssert verifyPriceIsCorrect(Integer expectedPrice) {
        validationStep.info("მიმდინარეობს ფასის (Price) შემოწმება");
        Assert.assertEquals(currentProduct.getPrice(), expectedPrice, "Price არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyDescriptionIsCorrect(String expectedDescription) {
        validationStep.info("მიმდინარეობს აღწერის (Description) შემოწმება");
        Assert.assertEquals(currentProduct.getDescription(), expectedDescription, "Description არასწორია");
        return this;
    }


    public ResponseProductDtoAssert verifyCategoryIdIsCorrect(Integer expectedCategoryId) {
        validationStep.info("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
        Assert.assertEquals(currentProduct.getCategory().getId(), expectedCategoryId, "Category ID არასწორია");
        return this;
    }


    public ResponseProductDtoAssert verifyImagesAreCorrect(List<String> expectedImages) {
        validationStep.info("მიმდინარეობს ფოტოების (Images) შემოწმება");
        Assert.assertEquals(currentProduct.getImages(), expectedImages, "Images არასწორია");
        return this;
    }



}