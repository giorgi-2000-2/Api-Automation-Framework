package ge.gmikeladze.platzi.assertions.assertsbusiness;
import com.aventstack.extentreports.ExtentTest;
import ge.gmikeladze.platzi.utils.ITestReporter;
import ge.gmikeladze.platzi.utils.ReportStatus;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class BaseAssert<T, Self extends BaseAssert<T, Self>>
        implements IBaseAssert<T, Self> {
    private final ITestReporter reporter;
    protected final SoftAssert softAssert;
    private final String nodeName;
    private ExtentTest node;

    protected T dto;
    protected List<T> dtoList;
    protected Response rawResponse;

    protected BaseAssert(ITestReporter reporter, SoftAssert softAssert, String nodeName) {
        this.reporter = reporter;
        this.softAssert = softAssert;
        this.nodeName = nodeName;

        reporter.createNode(nodeName);
    }



    public Self assertThat(T dto) {
        Assert.assertNotNull(dto, "DTO არ უნდა იყოს null");
        this.dto = dto;
        return (Self) this;
    }


    public Self assertThat(List<T> dtoList) {
        Assert.assertNotNull(dtoList, "DTO List არ უნდა იყოს null");
        this.dtoList = dtoList;
        return (Self) this;
    }


    public Self assertThat(Response response) {
        Assert.assertNotNull(response, "API Response არ უნდა იყოს null");
        this.rawResponse = response;
        Self self = (Self) this;
        return self;
    }


    public <V> Self hasField(Function<T, V> extractor, V expected, String fieldName) {
        step(fieldName + "-ის შემოწმება");
        V actual = extractor.apply(dto);
        softAssert.assertEquals(actual, expected, fieldName + " არასწორია");
        return (Self) this;
    }



    public <V> Self hasField(Function<T, V> extractor, V expected, String fieldName, String customMessage) {
        step(fieldName + "-ის შემოწმება");
        V actual = extractor.apply(dto);
        softAssert.assertEquals(actual, expected, customMessage);
        return (Self) this;
    }



    public <V> Self hasNotNullField(Function<T, V> extractor, String fieldName) {
        step(fieldName + "-ის NotNull შემოწმება");
        V actual = extractor.apply(dto);
        softAssert.assertNotNull(actual, fieldName + " არ უნდა იყოს null");
        return (Self) this;
    }


    public Self hasFieldMatching(Function<T, ?> extractor, Predicate<Object> predicate, String fieldName) {
        step(fieldName + "-ის პირობის შემოწმება");
        Object actual = extractor.apply(dto);
        softAssert.assertTrue(predicate.test(actual), fieldName + " პირობას არ აკმაყოფილებს. მნიშვნელობა: " + actual);
        return (Self) this;
    }



    public Self hasSize(int expectedSize) {
        step("სიის ზომის შემოწმება");
        softAssert.assertEquals(dtoList.size(), expectedSize, "სიის ზომა არასწორია");
        return (Self) this;
    }


    public Self allMatch(Predicate<T> predicate, String description) {
        step("ყველა ელემენტის პირობის შემოწმება: " + description);
        for (int i = 0; i < dtoList.size(); i++) {
            T item = dtoList.get(i);
            softAssert.assertTrue(predicate.test(item),
                    "ელემენტი #" + i + " არ აკმაყოფილებს პირობას: " + description);
        }
        return (Self) this;
    }

    public void hasNotNullFields(String description, Function<T, ?>... extractors) {
        step(description);
        for (Function<T, ?> extractor : extractors) {
            Object value = extractor.apply(dto);
            softAssert.assertNotNull(value, "ველი არ უნდა იყოს null");
        }
    }


    public Self allHavePositiveId(Function<T, Integer> idExtractor) {
        return allMatch(item -> {
            Integer id = idExtractor.apply(item);
            boolean valid = id != null && id > 0;
            if (valid) {
                log(ReportStatus.PASS, "ID ვალიდურია: " + id);
            } else {
                log(ReportStatus.FAIL, "არავალიდური ID: " + id);
            }
            return valid;
        }, "ID უნდა იყოს 0-ზე მეტი");
    }



    public void isDeletedSuccessfully() {
        step("შემოწმება: წაშლის სტატუსი");
        Assert.assertNotNull(rawResponse, "Response ობიექტი ცარიელია");
        Object boolResponse = rawResponse.jsonPath().get();
        softAssert.assertEquals(boolResponse, Boolean.TRUE,
                "წაშლის პასუხი უნდა იყოს true, დაბრუნდა: " + boolResponse);
    }


    protected void step(String message) {
        log(ReportStatus.INFO, message);
    }

    protected void log(ReportStatus status, String message) {
        reporter.log(status, message);
    }





}