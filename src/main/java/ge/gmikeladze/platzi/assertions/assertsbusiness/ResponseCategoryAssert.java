package ge.gmikeladze.platzi.assertions.assertsbusiness;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.utils.ITestReporter;
import org.testng.asserts.SoftAssert;

@TestScoped
public class ResponseCategoryAssert extends BaseAssert<GetResponseCategoryDto, ResponseCategoryAssert> {

    @Inject
    public ResponseCategoryAssert(ITestReporter reporter,SoftAssert softAssert) {
        super(reporter, softAssert,"კატეგორიის ბიზნეს სცენარის შემოწმება");
    }

    public ResponseCategoryAssert hasName(String expectedName) {
        return hasField(GetResponseCategoryDto::getName, expectedName, "Name / Title");
    }

    public ResponseCategoryAssert hasId(Integer expectedId) {
        return hasField(GetResponseCategoryDto::getId, expectedId, "ID");
    }

    public ResponseCategoryAssert hasImage(String expectedImage) {
        return hasField(GetResponseCategoryDto::getImage, expectedImage, "Image");
    }

    public ResponseCategoryAssert assertCategoryValidator(int limit) {
        hasSize(limit);
        return allHavePositiveId(GetResponseCategoryDto::getId);
    }
}