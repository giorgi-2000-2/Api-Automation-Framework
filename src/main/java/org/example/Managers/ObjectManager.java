package org.example.Managers;
import org.example.ApiClient.ProductApiClient;
import org.example.ApiService.ApiAssertHelper;
import org.example.ApiService.ApiRequest;
import org.example.ApiClient.CategoryApiClient;
import org.example.AssertionManager.ResponseAssert;
import org.example.Steps.CategorySteps;
import org.example.Steps.ProductSteps;


public class ObjectManager {
private ApiRequest apiRequest;
private ResponseAssert apiAssertHelper;
private CategoryApiClient categoryApiClient;
private CategorySteps categorySteps;
private ProductApiClient productApiClient;
private ProductSteps productSteps;




public ResponseAssert getAssert(){
    if(apiAssertHelper==null){
        apiAssertHelper = new ResponseAssert();

    }
    return apiAssertHelper;
}


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

    public CategorySteps getCategorySteps() {
        if (categorySteps == null) {
            categorySteps = new CategorySteps(this);
        }
        return categorySteps;
    }
public ProductApiClient productApiClient(){
    if(productApiClient==null){
        productApiClient=new ProductApiClient(this);
    }
    return productApiClient;
}

public ProductSteps getProductSteps(){
    if(productSteps==null){
        productSteps=new ProductSteps(this);
    }

    return productSteps;
}



}
