package ge.gmikeladze.platzi;
import com.google.inject.Inject;
import com.google.inject.Provider;
import ge.gmikeladze.platzi.assertions.assertsbusiness.ResponseErrorAssert;
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
import ge.gmikeladze.platzi.utils.ITestReporter;
import ge.gmikeladze.platzi.utils.ReportStatus;
import ge.gmikeladze.platzi.utils.TestListenerManager;
import ge.gmikeladze.platzi.utils.TestReporterContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.lang.reflect.Method;


@Guice(modules = FrameworkModule.class)
@Listeners({TestListenerManager.class, SoftAssertListener.class})
public abstract class BaseApiTest {
    @Inject private Provider<TestDataPreparer> dataPreparer;
    @Inject protected ITestReporter reporter;
    @Inject protected CategoryDataFactory categoryData;
    @Inject protected ProductDataFactory productData;
    @Inject protected UserDataFactory userData;
    @Inject protected Provider<CategorySteps> categorySteps;
    @Inject protected Provider<UserSteps> userSteps;
    @Inject protected Provider<ProductSteps> productSteps;
    @Inject protected Provider<SoftAssert> soft;
    @Inject protected Provider<TestContext> context;
    @Inject protected Provider<ResponseCategoryAssert> categoryAssert;
    @Inject protected Provider<ResponseProductAssert> productAssert;
    @Inject protected Provider<ResponseUserAssert> userAssert;
    @Inject protected Provider<ResponseErrorAssert> errorAssert;
    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, ITestResult result) {
        FrameworkModule.TEST_SCOPE.enter();

        TestReporterContext.set(reporter);

        reporter.createTest(displayName(method, result));
        TestReporterContext.get().info("ტესტი დაიწყო: " + displayName(method, result));
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
            context.get()
                    .getCleanupRegistry()
                    .cleanup();
        } catch (Throwable cleanupError) {
            TestReporterContext.get().log(
                    ReportStatus.WARNING,
                    "ტესტ მონაცემების გასუფთავება ვერ შესრულდა "
                            + cleanupError.getMessage()
            );
        } finally {
            try {
                FrameworkModule.TEST_SCOPE.exit();
            } finally {
                TestReporterContext.get().unload();
                TestReporterContext.remove();
            }
        }
    }
    @AfterClass(alwaysRun = true)
    public void tearDownAfterClass() {
                reporter.flush();
    }

}