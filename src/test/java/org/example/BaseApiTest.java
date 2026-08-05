package org.example;
import org.example.ApiService.HttpStatusCode;
import org.example.DTOs.RequestDto.*;
import org.example.DTOs.ResponseDto.*;
import org.example.Managers.*;
import org.example.ApiService.ApiConfig;
import org.example.annotations.RequiresCategory;
import org.example.Utils.ExtentReportManager;
import org.example.annotations.RequiresProduct;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.asserts.SoftAssert;
import java.lang.reflect.Method;


public abstract class BaseApiTest {
    private static final ThreadLocal<ObjectManager> apiObjectManagerThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<FactoryManager> factoryManagerThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<AssertionManager> assertionManagerThreadLocal = new ThreadLocal<>();
    private final ThreadLocal<SoftAssert>softAssertThreadLocal = new ThreadLocal<>();
    protected ThreadLocal <GetResponseCategoryDto> category = new ThreadLocal<>();
    protected ThreadLocal <CreateCategoryRequestDto> requestBodyCategory = new ThreadLocal<>();
    protected ThreadLocal <GetResponseProductDto> product = new ThreadLocal<>();
    protected ThreadLocal <CreateProductRequestDto> requestBodyProduct = new ThreadLocal<>();


    @BeforeMethod(alwaysRun = true)
    public void apiSetUp(Method method) {
        ExtentReportManager.createTest(method.getName());
        softAssertThreadLocal.set(new SoftAssert());
        if (assertionManagerThreadLocal.get()==null){
            assertionManagerThreadLocal.set(new AssertionManager(getSoft()));
        }
        if (apiObjectManagerThreadLocal.get() ==null){
            apiObjectManagerThreadLocal.set(new ObjectManager(assertManager()));

        }

        if(factoryManagerThreadLocal.get()==null){
            factoryManagerThreadLocal.set(new FactoryManager());
        }


        if (method.isAnnotationPresent(RequiresCategory.class)){
            requestBodyCategory.set(factory().categoryFactory().createCategoryWithData());
            this.category.set(api().getCategorySteps().createCategory(requestBodyCategory.get()));
        }


        if (method.isAnnotationPresent(RequiresCategory.class)&&method.isAnnotationPresent(RequiresProduct.class)){
            this.requestBodyProduct.set(factory().productFactory().createProductWithData(category.get().getId()));
            this.product.set(api().getProductSteps().createProduct(requestBodyProduct.get(), HttpStatusCode.CREATED));
        }
    }








    protected AssertionManager assertManager(){
        return assertionManagerThreadLocal.get();
    }


    protected SoftAssert getSoft(){
        return softAssertThreadLocal.get();
    }


    protected ObjectManager api() {
        return apiObjectManagerThreadLocal.get();
    }



    protected FactoryManager factory(){
        return factoryManagerThreadLocal.get();
    }


    @AfterMethod(alwaysRun = true)
    public void removeApi() {
        getSoft().assertAll();
        softAssertThreadLocal.remove();
        category.remove();
        product.remove();
        ApiConfig.clearSpec();
        assertionManagerThreadLocal.remove();
        apiObjectManagerThreadLocal.remove();
    }




    @AfterClass(alwaysRun = true)
    public void afterClass(){
        factoryManagerThreadLocal.remove();
    }


}  