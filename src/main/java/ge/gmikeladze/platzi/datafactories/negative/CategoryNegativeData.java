package ge.gmikeladze.platzi.datafactories.negative;
import static ge.gmikeladze.platzi.datafactories.negative.NegativeCase.of;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.RandomDataFactory;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.response.error.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.error.InternalServerErrorDto;
import ge.gmikeladze.platzi.dtos.response.error.PutBadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.error.ValidationErrorDto;
import ge.gmikeladze.platzi.utils.ConfigReader;
import org.testng.annotations.DataProvider;
@Singleton
public class CategoryNegativeData{

    private final RandomDataFactory randomDataFactory;
    @Inject
    public CategoryNegativeData(RandomDataFactory randomDataFactory) {
        this.randomDataFactory = randomDataFactory;
    }

    private  String image() {
        return ConfigReader.get("categoryImage");
    }

    private  String validName() {
        return  randomDataFactory.uniqueTitle(ConfigReader.get("categoryName"));
    }

    private  CreateCategoryRequestDto.CreateCategoryRequestDtoBuilder validCreate() {
        return CreateCategoryRequestDto.builder()
                .name(validName())
                .image(image());
    }

    private  UpdateCategoryRequestDto.UpdateCategoryRequestDtoBuilder validUpdate() {
        return UpdateCategoryRequestDto.builder()
                .name(validName())
                .image(image());
    }

    @DataProvider(name = "invalidCategoryCreate")
    public Object[][] invalidCategoryCreate() {
        return new Object[][]{

                {of("image — არავალიდური URL",
                        validCreate().image("not-a-url").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "image")},

                {of("image — ცარიელი",
                        validCreate().image("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "image")},

                {of("image — არასრული URL",
                        validCreate().image("http://").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "image")},


                {of("image — null (NOT NULL)",
                        validCreate().image(null).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "image")},


                {of("name — null → 500",
                        validCreate().name(null).build(),
                        HttpStatusCode.SERVER_ERROR, InternalServerErrorDto.class, "Internal")},

                {of("name და image — ორივე null → 500",
                        CreateCategoryRequestDto.builder().name(null).image(null).build(),
                        HttpStatusCode.SERVER_ERROR, InternalServerErrorDto.class, "Internal")},
        };
    }



    @DataProvider(name = "invalidCategoryUpdate")
    public Object[][] invalidCategoryUpdate() {
        return new Object[][]{

                {of("update image — არავალიდური URL",
                        validUpdate().image("not-a-url").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "image")},

                {of("update image — ცარიელი",
                        validUpdate().image("").build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "image")},


                {of("update image — null (NOT NULL)",
                        validUpdate().image(null).build(),
                        HttpStatusCode.BAD_REQUEST, PutBadRequestResponse.class, "image")},


                {of("update name — null → 500",
                        validUpdate().name(null).build(),
                        HttpStatusCode.BAD_REQUEST, PutBadRequestResponse.class, "name")},
        };
    }

    @DataProvider(name = "invalidCategoryId")
    public Object[][] invalidCategoryId() {
        return new Object[][]{
                {of("id = 0", 0,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "Category")},

                {of("id = -1", -1,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "Category", "-1")},

                {of("id = Integer.MAX_VALUE (არარსებული)", Integer.MAX_VALUE,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "Category", "2147483647")},
        };
    }

    @DataProvider(name = "invalidCategorySlug")
    public Object[][] invalidCategorySlug() {
        return new Object[][]{
                {of("slug — ცარიელი", "",
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class,
                        "Validation failed")},

                {of("slug — მხოლოდ ჰარი", " ",
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "Category", "slug")},

                {of("slug — არარსებული", "no-such-slug-" + randomDataFactory.uniqueTitle("no-such-slug"),
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class,
                        "Category", "no-such-slug")},
        };
    }
}