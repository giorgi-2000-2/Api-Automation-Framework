package ge.gmikeladze.platzi.di;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.cleanup.CleanupRegistry;
import ge.gmikeladze.platzi.utils.LogFilter;
import ge.gmikeladze.platzi.utils.config.IConfigForData;
import ge.gmikeladze.platzi.utils.config.IConfigForRequest;
import ge.gmikeladze.platzi.utils.config.PropertiesConfig;
import ge.gmikeladze.platzi.utils.metrics.*;
import ge.gmikeladze.platzi.utils.metrics.generator.MetricsHistoryStore;
import ge.gmikeladze.platzi.utils.metrics.generator.MetricsHtmlGenerator;
import ge.gmikeladze.platzi.utils.metrics.generator.MetricsJsonReportBuilder;
import ge.gmikeladze.platzi.utils.reporter.extent.ExtentTestReporter;
import ge.gmikeladze.platzi.utils.reporter.IReportConfig;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import ge.gmikeladze.platzi.utils.reporter.allure.AllureTestReporter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.asserts.SoftAssert;


public class FrameworkModule extends AbstractModule {
    public final TestScope TEST_SCOPE = new TestScope();

    @Override
    protected void configure() {
        bindScope(TestScoped.class, TEST_SCOPE);
        bind(TestScope.class).toInstance(TEST_SCOPE);
        bind(SoftAssert.class).in(TEST_SCOPE);

        bind(CleanupRegistry.class).in(TEST_SCOPE);

        bind(IConfigForRequest.class).to(PropertiesConfig.class);
        bind(IConfigForData.class).to(PropertiesConfig.class);
        bind(IReportConfig.class).to(PropertiesConfig.class);


        bind(MetricsRegistry.class).asEagerSingleton();
        bind(MetricsFilter.class).asEagerSingleton();
        bind(MetricsHistoryStore.class).asEagerSingleton();
        bind(MetricsJsonReportBuilder.class).asEagerSingleton();
        bind(MetricsHtmlGenerator.class).asEagerSingleton();
        bind(MetricsReportService.class).asEagerSingleton();
        bind(SuiteMetricsListener.class).asEagerSingleton();
        bind(PercentileCalculator.class).asEagerSingleton();
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
    }


    @Provides
    @Singleton
    ITestReporter provideReporter(IReportConfig config,
                                  Provider<ExtentTestReporter> extent,
                                  Provider<AllureTestReporter> allure) {
        return switch (config.reportEngine()) {
            case EXTENT -> extent.get();
            case ALLURE -> allure.get();
        };
    }

    @Provides
    @Singleton
    RequestSpecification provideRequestSpec(ITestReporter reporter,
                                            IConfigForRequest config,
                                            MetricsFilter metricsFilter) {
        return new RequestSpecBuilder()
                .setBaseUri(config.baseUrl())
                .setContentType("application/json")
                .addFilter(new LogFilter(reporter))
                .addFilter(metricsFilter)
                .build();
    }
}