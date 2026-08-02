package org.example;
import org.example.DTOs.RequestDto.*;
import org.example.DTOs.ResponseDto.*;
import org.example.Managers.*;
import org.example.Utils.ApiConfig;
import org.example.annotations.RequiresCategory;
import org.example.Utils.ExtentReportManager;
import org.example.annotations.RequiresProduct;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.lang.reflect.Method;


public class BaseApiTest {
    private static final ThreadLocal<ObjectManager> apiObjectManagerThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<FactoryManager> factoryManagerThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<AssertionManager> assertionManagerThreadLocal = new ThreadLocal<>();
    protected ThreadLocal <GetResponseCategoryDTO> category = new ThreadLocal<>();
    protected ThreadLocal <CreateCategoryRequestDto> requestBodyCategory = new ThreadLocal<>();
    protected ThreadLocal <GetResponseProductDto> product = new ThreadLocal<>();
    protected ThreadLocal <CreateProductRequestDto> requestBodyProduct = new ThreadLocal<>();


    @BeforeMethod(alwaysRun = true)
    public void apiSetUp(Method method) {
        ExtentReportManager.createTest(method.getName());
        if (apiObjectManagerThreadLocal.get() ==null){
        apiObjectManagerThreadLocal.set(new ObjectManager());

        }
        if (assertionManagerThreadLocal.get()==null){
            assertionManagerThreadLocal.set(new AssertionManager());
        }

        if(factoryManagerThreadLocal.get()==null){
            factoryManagerThreadLocal.set(new FactoryManager());
        }
            if (method.isAnnotationPresent(RequiresCategory.class)){
                requestBodyCategory.set(factory().categoryFactory().createCategoryWithData());
                this.category.set(api().getCategorySteps().createCategorySuccessfully(requestBodyCategory.get()));

            }
        if (method.isAnnotationPresent(RequiresCategory.class)&&method.isAnnotationPresent(RequiresProduct.class)){

            this.requestBodyProduct.set(factory().productFactory().createProductWithData(category.get().getId()));
            this.product.set( api().getProductSteps().createProductSuccessfully(requestBodyProduct.get()));



        }
        
        
    }


protected AssertionManager assertManager(){
        return assertionManagerThreadLocal.get();
}


    protected ObjectManager api() {
        return apiObjectManagerThreadLocal.get();
    }



protected FactoryManager factory(){
        return factoryManagerThreadLocal.get();
}


    @AfterMethod
    public void removeApi() {
            category.remove();
            product.remove();

        if (AuthManager.getToken()!=null){
            AuthManager.clear();
        }
if(ApiConfig.getBaseSpec()!=null){
    ApiConfig.clearSpec();
}
assertionManagerThreadLocal.remove();
    }

    @AfterClass
    public void afterClass(){

        factoryManagerThreadLocal.remove();


    }

}