package org.example.managers;
import org.example.assertionmanager.*;
import org.testng.asserts.SoftAssert;

public class AssertionManager  {
    private ResponseProductDtoAssert responseProductDtoAssert;
    private ResponseCategoryDtoAssert responseCategoryDtoAssert;
    private  ResponseValidator responseValidator;
    private SoftAssert softAssert;
public AssertionManager(SoftAssert softAssert){
    this.softAssert=softAssert;
}

    public ResponseProductDtoAssert getResponseProductDtoAssert(){

        if(responseProductDtoAssert==null){
            responseProductDtoAssert= new ResponseProductDtoAssert();
        }
        return responseProductDtoAssert;
    }

    public ResponseCategoryDtoAssert getResponseCategoryDtoAssert(){

        if(responseCategoryDtoAssert==null){
            responseCategoryDtoAssert= new ResponseCategoryDtoAssert();
        }
        return responseCategoryDtoAssert;
    }

    public   ResponseValidator getResponseValidator(){
        if(responseValidator==null){
            responseValidator = new ResponseValidator(softAssert);
        }
        return responseValidator;
    }


}