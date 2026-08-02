package org.example.Managers;
import org.example.AssertionManager.ResponseCategoryDtoAssert;
import org.example.AssertionManager.ResponseProductDtoAssert;

public class AssertionManager {
    private ResponseProductDtoAssert responseProductDtoAssert;
    private ResponseCategoryDtoAssert responseCategoryDtoAssert;




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


}
