package org.example.assertionmanager;
import com.aventstack.extentreports.ExtentTest;
import org.example.dtos.responsedto.GetResponseCategoryDto;
import org.testng.Assert;
import java.util.List;
import static org.example.utils.ExtentReportManager.getTest;

public class ResponseCategoryDtoAssert {
    private GetResponseCategoryDto currentCategory;


    private  List<GetResponseCategoryDto> responseCategoryDtos;
    ExtentTest validationStep = getTest().createNode(" კატეგორიის ბიზნეს სცენარის შემოწმება ");

    public ResponseCategoryDtoAssert assertThat(GetResponseCategoryDto requestBody) {
        this.currentCategory =requestBody;
        return this;
    }
    public ResponseCategoryDtoAssert assertThat(List<GetResponseCategoryDto> responseCategoryDtos) {
        this.responseCategoryDtos =responseCategoryDtos;
        return this;
    }

    public ResponseCategoryDtoAssert verifyTitleIsCorrect(String expectedTitle) {
        validationStep.info(" მიმდინარეობს კატეგორიის Title-ის შემოწმება ");
        Assert.assertEquals(currentCategory.getName(), expectedTitle, " Title არასწორია ");
        return this;
    }


    public ResponseCategoryDtoAssert verifyIdIsCorrect(int expectedId) {
        getTest().info("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
        Assert.assertEquals(currentCategory.getId(), expectedId, "Category ID არასწორია");
        return this;
    }

    public ResponseCategoryDtoAssert verifyImageIsCorrect(String expectedImage) {
        validationStep.info("მიმდინარეობს კატეგორიის სურათის შემოწმება");
        Assert.assertEquals(currentCategory.getImage(), expectedImage, "Category image არასწორია");
        return this;
    }



    public void assertCategoryValidator() {

        for (GetResponseCategoryDto categorie : responseCategoryDtos) {

            int id = categorie.getId();

            if (id > 0) {
                getTest().pass("კატეგორიის ID ვალიდურია: " + id);
            } else {
                getTest().fail("არავალიდური კატეგორიის ID: " + id);
            }

            Assert.assertTrue(id > 0, "ID უნდა იყოს 0-ზე მეტი! ნაპოვნია: " + id);
        }
    }


}