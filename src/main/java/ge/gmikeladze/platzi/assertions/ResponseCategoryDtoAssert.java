package ge.gmikeladze.platzi.assertions;
import com.aventstack.extentreports.Status;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import java.util.List; 

@TestScoped
public class ResponseCategoryDtoAssert extends BaseAssert{
    private GetResponseCategoryDto currentCategory;
    private List<GetResponseCategoryDto> responseCategoryDtos;
    private Response responseCategory;


    @Inject
    ResponseCategoryDtoAssert(SoftAssert softAssert) {
        super(softAssert,"კატეგორიის ბიზნეს სცენარის შემოწმება");

      }


    public ResponseCategoryDtoAssert assertThat(Response response) {
        this.responseCategory = response;
        return this;
    }

    public ResponseCategoryDtoAssert assertThat(GetResponseCategoryDto requestBody) {
        this.currentCategory = requestBody;
        return this;
    }

    public ResponseCategoryDtoAssert assertThat(List<GetResponseCategoryDto> responseCategoryDtos) {
        this.responseCategoryDtos = responseCategoryDtos;
        return this;
    }

    public ResponseCategoryDtoAssert verifyTitleIsCorrect(String expectedTitle) {
        step("მიმდინარეობს კატეგორიის Title-ის შემოწმება");
        softAssert.assertEquals(currentCategory.getName(), expectedTitle, "Title არასწორია");
        return this;
    }

    public ResponseCategoryDtoAssert verifyIdIsCorrect(int expectedId) {

        step("მიმდინარეობს კატეგორიის ID-ს შემოწმება");
        Assert.assertEquals(currentCategory.getId(), expectedId, "Category ID არასწორია");
        return this;
    }

    public ResponseCategoryDtoAssert verifyImageIsCorrect(String expectedImage) {
        step("მიმდინარეობს კატეგორიის სურათის შემოწმება");
        softAssert.assertEquals(currentCategory.getImage(), expectedImage, "Category image არასწორია");
        return this;
    }

    public void verifyBooleanResponseIsCorrect() {
        step("მიმდინარეობს წაშლის შემოწმება");
        Object BoolResponse = responseCategory.jsonPath().get();
        softAssert.assertEquals(BoolResponse, Boolean.TRUE, "წაშლის პასუხი უნდა იყოს true, მიღებულია: " + BoolResponse);
    }

    public void assertCategoryValidator(int limit) {
        softAssert.assertEquals(responseCategoryDtos.size(),limit);
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



}
