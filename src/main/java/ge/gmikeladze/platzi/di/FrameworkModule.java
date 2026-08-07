package ge.gmikeladze.platzi.di;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import ge.gmikeladze.platzi.utils.ConfigReader;
import ge.gmikeladze.platzi.utils.LogFilter;
import org.testng.asserts.SoftAssert;

public class FrameworkModule extends AbstractModule {
    public static final TestScope TEST_SCOPE = new TestScope();

    @Override
    protected void configure() {
        bindScope(TestScoped.class, TEST_SCOPE);
        bind(SoftAssert.class).in(TEST_SCOPE);
    }
    @Provides
    @Singleton
    RequestSpecification provideRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("BASE_URL"))
                .setContentType("application/json")
                .addFilter(new LogFilter())
                .build();
    }
}