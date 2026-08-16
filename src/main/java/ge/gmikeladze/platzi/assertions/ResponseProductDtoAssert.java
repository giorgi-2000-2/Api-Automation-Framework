package ge.gmikeladze.platzi.assertions;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
import java.util.List;

@TestScoped
public class ResponseProductDtoAssert extends BaseAssert {
    private GetResponseProductDto currentProduct;
    private Response responseProduct;

    @Inject
    ResponseProductDtoAssert(SoftAssert softAssert) {
        super(softAssert,"პროდუქტის ბიზნეს სცენარის შემოწმება");

    }

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
        Object BoolResponse = responseProduct.jsonPath().get();
        softAssert.assertEquals(BoolResponse, Boolean.TRUE, "წაშლის პასუხი უნდა იყოს true, მიღებულია: " + BoolResponse);
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



}
