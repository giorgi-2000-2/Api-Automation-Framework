package org.example.Steps;
import io.restassured.response.Response;
import org.example.ApiService.HttpStatusCode;
import org.example.DTOs.RequestDto.CreateProductRequestDto;
import org.example.DTOs.ResponseDto.BadRequestResponse;
import org.example.DTOs.ResponseDto.GetResponseProductDto;
import org.example.DTOs.ResponseDto.PutBadRequestResponse;
import org.example.Managers.AssertionManager;
import org.example.Managers.ObjectManager;
import org.testng.asserts.SoftAssert;

public class ProductSteps {
    private ObjectManager api;
    private AssertionManager assertionManager;
    public ProductSteps (ObjectManager api, AssertionManager assertionManager){
        this.api=api;
        this.assertionManager =assertionManager;

    }

    public GetResponseProductDto createProductSuccessfully(CreateProductRequestDto requestBody) {
        Response response = api.getProductApiClient().createProduct(requestBody);

        SoftAssert softAssert = new SoftAssert();

        assertionManager.getValidator().ValidateJson(response,GetResponseProductDto.class,softAssert);
        softAssert.assertAll();
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.CREATED)
                .hasContentType("application/json")
                .time();

        return response.as(GetResponseProductDto.class);

    }

    public BadRequestResponse createProductWithWrongCategoryId(CreateProductRequestDto requestBody,SoftAssert softAssert) {
        Response response = api.getProductApiClient().createProduct(requestBody);

        assertionManager.getValidator().ValidateJson(response,BadRequestResponse.class,softAssert);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();

        return response.as(BadRequestResponse.class);

    }

public GetResponseProductDto getProductById(int id,SoftAssert softAssert ){
        Response response = api.getProductApiClient().getProductById(id);

    assertionManager.getValidator().ValidateJson(response,GetResponseProductDto.class,softAssert);
    assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .hasContentType("application/json")
            .time();

        return response.as(GetResponseProductDto.class);
}

    public BadRequestResponse getProductByWrongId(int id){
        Response response = api.getProductApiClient().getProductById(id);
        SoftAssert softAssert = new SoftAssert();

        assertionManager.getValidator().ValidateJson(response,BadRequestResponse.class,softAssert);
        softAssert.assertAll();
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();

        return response.as(BadRequestResponse.class);

    }


    public GetResponseProductDto putProduct(int id, Object body,SoftAssert softAssert){

        Response response = api.getProductApiClient().putProductById(id, body);

        assertionManager.getValidator().ValidateJson(response,GetResponseProductDto.class,softAssert);

        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .hasContentType("application/json")
                .time();
        return response.as(GetResponseProductDto.class);

    }

    public PutBadRequestResponse putProductBadRequest(int id, Object body,SoftAssert softAssert){

        Response response = api.getProductApiClient().putProductById(id, body);


        assertionManager.getValidator().ValidateJson(response, PutBadRequestResponse.class,softAssert);

        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();
        return response.as(PutBadRequestResponse.class);

    }



    public Response deleteProduct(int id){
        Response response =  api.getProductApiClient().deleteProductById(id);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .time();
        return response;



    }


    public BadRequestResponse deleteWithWrongCategoryId(int id,SoftAssert softAssert){
        Response response = api.getProductApiClient().deleteProductById(id);

        assertionManager.getValidator().ValidateJson(response,BadRequestResponse.class,softAssert);
        softAssert.assertAll();
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .time();
        return response.as(BadRequestResponse.class);
    }

}
