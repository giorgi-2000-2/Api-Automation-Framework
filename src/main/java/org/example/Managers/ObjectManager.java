package org.example.Managers;
import org.example.ApiClient.ProductApiClient;
import org.example.ApiService.ApiRequest;
import org.example.ApiClient.CategoryApiClient;
import org.example.Steps.CategorySteps;
import org.example.Steps.ProductSteps;


public class ObjectManager {
private ApiRequest apiRequest;
private CategoryApiClient categoryApiClient;
private CategorySteps categorySteps;
private ProductApiClient productApiClient;
private ProductSteps productSteps;
    private AssertionManager assertionManager;


    public ApiRequest getRequest() {
        if (apiRequest == null) {
            apiRequest = new ApiRequest(this);
        }
        return apiRequest;
    }

    public CategoryApiClient getCategoryClient() {
        if (categoryApiClient == null) {
            categoryApiClient = new CategoryApiClient(this);
        }
        return categoryApiClient;
    }

public ProductApiClient getProductApiClient(){
    if(productApiClient==null){
        productApiClient=new ProductApiClient(this);
    }
    return productApiClient;
}
public AssertionManager getAssert(){
        if(assertionManager==null){
            assertionManager=new AssertionManager();
        }

        return assertionManager;
}

    public CategorySteps getCategorySteps() {
        if (categorySteps == null) {
            categorySteps = new CategorySteps(this,getAssert());
        }
        return categorySteps;
    }


public ProductSteps getProductSteps(){
    if(productSteps==null){
        productSteps=new ProductSteps(this,getAssert());
    }

    return productSteps;
}



}
