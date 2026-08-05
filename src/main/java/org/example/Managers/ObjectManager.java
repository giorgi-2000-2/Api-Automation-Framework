package org.example.Managers;
import org.example.ApiClient.GenericClient;
import org.example.ApiService.ApiRequest;
import org.example.Steps.CategorySteps;
import org.example.Steps.ProductSteps;


public class ObjectManager {
    private ApiRequest apiRequest;
    private CategorySteps categorySteps;
    private ProductSteps productSteps;
    private AssertionManager assertionManager;
private GenericClient genericClient;
   public ObjectManager(AssertionManager assertionManager){

       this.assertionManager = assertionManager;
   }

    public GenericClient getGenericClient() {
        if(genericClient==null){
            genericClient=new GenericClient(getRequest());
        }
        return genericClient;
    }

    public ApiRequest getRequest() {
        if (apiRequest == null) {
            apiRequest = new ApiRequest();
        }
        return apiRequest;
    }

    public CategorySteps getCategorySteps() {
        if (categorySteps == null) {
            categorySteps = new CategorySteps(getGenericClient(), assertionManager.getResponseValidator());
        }
        return categorySteps;
    }

    public ProductSteps getProductSteps() {
        if (productSteps == null) {
            productSteps = new ProductSteps(getGenericClient(), assertionManager.getResponseValidator());
        }
        return productSteps;
    }




}