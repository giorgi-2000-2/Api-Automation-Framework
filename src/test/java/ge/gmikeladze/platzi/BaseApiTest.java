package ge.gmikeladze.platzi;
import com.aventstack.extentreports.Status;
import com.google.inject.Inject;
import com.google.inject.Provider;
import ge.gmikeladze.platzi.assertions.ResponseErrorAssert;
import ge.gmikeladze.platzi.assertions.assertsbusiness.ResponseCategoryAssert;
import ge.gmikeladze.platzi.assertions.assertsbusiness.ResponseProductAssert;
import ge.gmikeladze.platzi.assertions.assertsbusiness.ResponseUserAssert;
import ge.gmikeladze.platzi.datafactories.CategoryDataFactory;
import ge.gmikeladze.platzi.datafactories.ProductDataFactory;
import ge.gmikeladze.platzi.datafactories.UserDataFactory;
import ge.gmikeladze.platzi.di.FrameworkModule;
import ge.gmikeladze.platzi.di.SoftAssertListener;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.steps.CategorySteps;
import ge.gmikeladze.platzi.steps.ProductSteps;
import ge.gmikeladze.platzi.steps.UserSteps;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import ge.gmikeladze.platzi.utils.TestListenerManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.annotations.Listeners;
import org.testng.asserts.SoftAssert;
import java.lang.reflect.Method;


@Guice(modules = FrameworkModule.class)
@Listeners({TestListenerManager.class, SoftAssertListener.class})
public abstract class BaseApiTest {
    @Inject private Provider<TestDataPreparer> dataPreparer;

    @Inject protected CategoryDataFactory categoryData;
    @Inject protected ProductDataFactory productData;
    @Inject protected UserDataFactory userData;

    @Inject protected Provider<ResponseErrorAssert> errorAssert;
    @Inject protected Provider<CategorySteps> categorySteps;
    @Inject protected Provider<UserSteps> userSteps;
    @Inject protected Provider<ProductSteps> productSteps;

    @Inject protected Provider<SoftAssert> soft;
    @Inject protected Provider<TestContext> context;

    @Inject protected Provider<ResponseCategoryAssert> categoryAssert;
    @Inject protected Provider<ResponseProductAssert> productAssert;
    @Inject protected Provider<ResponseUserAssert> userAssert;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, ITestResult result) {
        FrameworkModule.TEST_SCOPE.enter();
        ExtentReportManager.createTest(displayName(method, result));
        result.setAttribute("softAssert", soft.get());
        dataPreparer.get().prepare(method);
    }


    private String displayName(Method method, ITestResult result) {
        Object[] params = result.getParameters();
        if (params == null || params.length == 0) {
            return method.getName();
        } else {return method.getName() + " " + params[0] + " ";}

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        try {
            context.get().getCleanupRegistry().cleanup();
        } catch (Throwable cleanupError) {
            ExtentReportManager.log(Status.WARNING,
                    "ტესტ მონაცემების გასუფთავება ვერ შესრულდა " + cleanupError.getMessage());
        } finally {
            try {
                FrameworkModule.TEST_SCOPE.exit();
            } finally {
                ExtentReportManager.unload();
            }
        }
    }



}