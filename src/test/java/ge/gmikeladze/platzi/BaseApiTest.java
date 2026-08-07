package ge.gmikeladze.platzi;

import com.google.inject.Inject;
import com.google.inject.Provider;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseCategoryDtoAssert;
import ge.gmikeladze.platzi.assertions.ResponseProductDtoAssert;
import ge.gmikeladze.platzi.datafactories.CategoryDataFactory;
import ge.gmikeladze.platzi.datafactories.ProductDataFactory;
import ge.gmikeladze.platzi.datafactories.RandomDataFactory;
import ge.gmikeladze.platzi.di.FrameworkModule;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.steps.CategorySteps;
import ge.gmikeladze.platzi.steps.ProductSteps;
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Guice;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Method;

@Guice(modules = FrameworkModule.class)
public abstract class BaseApiTest {


    @Inject protected CategoryDataFactory categoryData;
    @Inject protected ProductDataFactory  productData;
    @Inject protected RandomDataFactory   randomData;

    @Inject protected Provider<CategorySteps> categorySteps;
    @Inject protected Provider<ProductSteps>  productSteps;
    @Inject protected Provider<SoftAssert>    soft;
    @Inject protected Provider<TestContext>   context;
    @Inject protected Provider<ResponseCategoryDtoAssert> categoryAssert;
    @Inject protected Provider<ResponseProductDtoAssert>  productAssert;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method, ITestResult result) {
        FrameworkModule.TEST_SCOPE.enter();
        ExtentReportManager.createTest(method.getName());


        result.setAttribute("softAssert", soft.get());

        boolean needsCategory = method.isAnnotationPresent(RequiresCategory.class);
        boolean needsProduct  = method.isAnnotationPresent(RequiresProduct.class);

        if (needsProduct && !needsCategory) {
            throw new IllegalStateException(
                    "@RequiresProduct მოითხოვს @RequiresCategory-ს: " + method.getName());
        }

        TestContext ctx = context.get();

        if (needsCategory) {
            ctx.setCategoryRequest(categoryData.createCategoryWithData());
            ctx.setCategory(categorySteps.get().createCategory(ctx.getCategoryRequest()));
        }
        if (needsProduct) {
            ctx.setProductRequest(productData.createProductWithData(ctx.getCategory().getId()));
            ctx.setProduct(productSteps.get()
                    .createProduct(ctx.getProductRequest(), HttpStatusCode.CREATED));
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        FrameworkModule.TEST_SCOPE.exit();
    }
}
