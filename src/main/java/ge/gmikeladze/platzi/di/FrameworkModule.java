package ge.gmikeladze.platzi.di;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.cleanup.CleanupRegistry;
import ge.gmikeladze.platzi.utils.config.IConfigForData;
import ge.gmikeladze.platzi.utils.config.IConfigForRequest;
import ge.gmikeladze.platzi.utils.config.PropertiesConfig;
import ge.gmikeladze.platzi.utils.reporter.ExtentTestReporter;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import ge.gmikeladze.platzi.utils.LogFilter;
import org.testng.asserts.SoftAssert;


public class FrameworkModule extends AbstractModule {
    public static final TestScope TEST_SCOPE = new TestScope();


    public FrameworkModule() {
    }

    @Override
    protected void configure() {
        bindScope(TestScoped.class, TEST_SCOPE);

        bind(SoftAssert.class)
                .in(TEST_SCOPE);

        bind(CleanupRegistry.class)
                .in(TEST_SCOPE);

        bind(ITestReporter.class)
                .to(ExtentTestReporter.class)
                .in(Singleton.class);

        bind(PropertiesConfig.class).in(Singleton.class);

        bind(IConfigForRequest.class).to(PropertiesConfig.class);

        bind(IConfigForData.class).to(PropertiesConfig.class);
    }

    @Provides
    @Singleton
    RequestSpecification provideRequestSpec(
            ITestReporter reporter,
            IConfigForRequest config
    ) {
        return new RequestSpecBuilder()
                .setBaseUri(config.baseUrl())
                .setContentType("application/json")
                .addFilter(new LogFilter(reporter))
                .build();
    }
}