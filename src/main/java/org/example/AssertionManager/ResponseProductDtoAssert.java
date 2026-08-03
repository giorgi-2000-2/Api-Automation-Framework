package org.example.AssertionManager;
import com.aventstack.extentreports.ExtentTest;
import io.restassured.response.Response;
import org.example.DTOs.ResponseDto.GetResponseCategoryDTO;
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
    public ResponseProductDtoAssert verifyIdIsCorrect(Integer expectedId) {
        validationStep.info("მიმდინარეობს ID-ს შემოწმება");
            Assert.assertEquals(currentProduct.getId(), expectedId, "ID არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyTitleIsCorrect(String expectedTitle) {
        validationStep.info("მიმდინარეობს სათაურის (Title) შემოწმება");
        Assert.assertEquals(currentProduct.getTitle(), expectedTitle, "Title არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifySlugIsCorrect(String expectedSlug) {
        validationStep.info("მიმდინარეობს Slug-ის შემოწმება");
        Assert.assertEquals(currentProduct.getSlug(), expectedSlug, "Slug არასწორია");
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

    public ResponseProductDtoAssert verifyCategoryIsCorrect(GetResponseCategoryDTO expectedCategory) {
        validationStep.info("მიმდინარეობს კატეგორიის (Category) შემოწმება");
        Assert.assertEquals(currentProduct.getCategory(), expectedCategory, "Category არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyCategoryIdIsCorrect(Integer expectedCategoryId) {
        validationStep.info("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
           Assert.assertEquals(currentProduct.getCategory().getId(), expectedCategoryId, "Category ID არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyCategoryIdIsWrong(GetResponseCategoryDTO expectedCategoryId) {
        validationStep.info("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
        Assert.assertTrue(expectedCategoryId.getId()<=0, "Category ID არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyImagesAreCorrect(List<String> expectedImages) {
        validationStep.info("მიმდინარეობს ფოტოების (Images) შემოწმება");
        Assert.assertEquals(currentProduct.getImages(), expectedImages, "Images არასწორია");
         return this;
    }

    public ResponseProductDtoAssert verifyCreationAtIsCorrect(String expectedCreationAt) {
        validationStep.info("მიმდინარეობს შექმნის თარიღის (CreationAt) შემოწმება");
           Assert.assertEquals(currentProduct.getCreationAt(), expectedCreationAt, "CreationAt არასწორია");
        return this;
    }

    public ResponseProductDtoAssert verifyUpdatedAtIsCorrect(String expectedUpdatedAt) {
        validationStep.info("მიმდინარეობს განახლების თარიღის (UpdatedAt) შემოწმება");
           Assert.assertEquals(currentProduct.getUpdatedAt(), expectedUpdatedAt, "UpdatedAt is wrong");
        return this;
    }


}
