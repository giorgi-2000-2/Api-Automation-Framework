package org.example.Managers;
import org.example.AssertionManager.ResponseAssert;
import org.example.AssertionManager.ResponseCategoryDtoAssert;
import org.example.AssertionManager.ResponseProductDtoAssert;
import org.example.AssertionManager.Validator;

public class AssertionManager  {
    private ResponseProductDtoAssert responseProductDtoAssert;
    private ResponseCategoryDtoAssert responseCategoryDtoAssert;
    private  Validator validator;
private ResponseAssert responseAssert;

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

    public Validator getValidator(){
        if(validator==null){
            validator = new Validator();
        }
        return validator;
    }

    public ResponseAssert getAssert(){
        if(responseAssert==null){
            responseAssert = new ResponseAssert();

        }
        return responseAssert;
    }


}
