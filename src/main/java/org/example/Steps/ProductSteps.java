package org.example.Steps;
import io.restassured.response.Response;
import org.example.ApiService.HttpStatusCode;
import org.example.DTOs.RequestDto.CreateProductRequestDto;
import org.example.DTOs.ResponseDto.BadRequestResponse;
import org.example.DTOs.ResponseDto.GetResponseProductDto;
import org.example.Managers.ObjectManager;

public class ProductSteps {
    private ObjectManager api;

    public ProductSteps (ObjectManager api){
        this.api=api;
    }

    public GetResponseProductDto createProductSuccessfully(CreateProductRequestDto requestBody) {
        Response response = api.productApiClient().createProduct(requestBody);

        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.CREATED)
                .hasContentType("application/json")
                .time();

        return response.as(GetResponseProductDto.class);

    }

    public BadRequestResponse createProductWithWrongCategoryId(CreateProductRequestDto requestBody) {
        Response response = api.productApiClient().createProduct(requestBody);

        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();

        return response.as(BadRequestResponse.class);

    }

public GetResponseProductDto getProductById(int id){
        Response response = api.productApiClient().getProductById(id);

    api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .hasContentType("application/json")
            .time();

        return response.as(GetResponseProductDto.class);
}

    public BadRequestResponse getProductByWrongId(int id){
        Response response = api.productApiClient().getProductById(id);

        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();

        return response.as(BadRequestResponse.class);

    }


    public GetResponseProductDto putProduct(int id, Object body){

        Response response = api.productApiClient().putProductById(id, body);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .hasContentType("application/json")
                .time();
        return response.as(GetResponseProductDto.class);

    }

    public BadRequestResponse putProductBadRequest(int id, Object body){

        Response response = api.productApiClient().putProductById(id, body);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();
        return response.as(BadRequestResponse.class);

    }



    public Response deleteProduct(int id){
        Response response =  api.productApiClient().deleteProductById(id);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .time();
        return response;



    }


    public BadRequestResponse deleteWithWrongCategoryId(int id){
        Response response = api.productApiClient().deleteProductById(id);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .time();
        return response.as(BadRequestResponse.class);
    }

}
