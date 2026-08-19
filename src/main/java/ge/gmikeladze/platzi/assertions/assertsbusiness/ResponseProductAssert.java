package ge.gmikeladze.platzi.assertions.assertsbusiness;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import ge.gmikeladze.platzi.utils.ITestReporter;
import org.testng.asserts.SoftAssert;
import java.util.List;

@TestScoped
public class ResponseProductAssert extends BaseAssert<GetResponseProductDto, ResponseProductAssert> {

    @Inject
    public ResponseProductAssert(ITestReporter reporter,SoftAssert softAssert) {
        super(reporter,softAssert, "პროდუქტის ბიზნეს სცენარის შემოწმება");
    }

    public ResponseProductAssert hasTitle(String expectedTitle) {
        return hasField(GetResponseProductDto::getTitle, expectedTitle, "Title");
    }

    public ResponseProductAssert hasPrice(Integer expectedPrice) {
        return hasField(GetResponseProductDto::getPrice, expectedPrice, "Price");
    }

    public ResponseProductAssert hasDescription(String expectedDescription) {
        return hasField(GetResponseProductDto::getDescription, expectedDescription, "Description");
    }

    public ResponseProductAssert hasCategoryId(Integer expectedCategoryId) {
        return hasField(p -> p.getCategory().getId(), expectedCategoryId, "Category ID");
    }

    public ResponseProductAssert hasImages(List<String> expectedImages) {
        return hasField(GetResponseProductDto::getImages, expectedImages, "Images");
    }

}