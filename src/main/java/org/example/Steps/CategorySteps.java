package org.example.Steps;
import io.restassured.response.Response;
import org.example.ApiService.HttpStatusCode;
import org.example.DTOs.RequestDto.CreateCategoryRequestDto;
import org.example.DTOs.RequestDto.GetCategoryLimitRequestDTO;
import org.example.DTOs.ResponseDto.BadRequestResponse;
import org.example.DTOs.ResponseDto.GetResponseCategoryDTO;
import org.example.DTOs.ResponseDto.DeleteCategoryResponseDto;
import org.example.DTOs.ResponseDto.PutBadRequestResponse;
import org.example.Managers.AssertionManager;
import org.example.Managers.ObjectManager;
import org.testng.asserts.SoftAssert;

public class CategorySteps {
    private ObjectManager api;
     private AssertionManager assertionManager;
    public CategorySteps(ObjectManager api, AssertionManager assertionManager) {
        this.api = api;
        this.assertionManager=assertionManager;
    }

    public GetResponseCategoryDTO createCategorySuccessfully(CreateCategoryRequestDto requestBody,SoftAssert softAssert) {
        Response response = api.getCategoryClient().createCategory(requestBody);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.CREATED)
                .hasContentType("application/json")
                .time();

        assertionManager.getValidator().ValidateJson(response, GetResponseCategoryDTO.class,softAssert);
        softAssert.assertAll();
        return response.as(GetResponseCategoryDTO.class);

    }


    public void createCategoryBadRequest(CreateCategoryRequestDto requestBody) {
        Response response = api.getCategoryClient().createCategory(requestBody);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();
    }

public Response getCategoriesByLimit(GetCategoryLimitRequestDTO limit ) {
    Response response= api.getCategoryClient().getCategoryLimit(limit);
    assertionManager.getAssert().assertThat(response)
            .hasStatusCode(HttpStatusCode.OK)
            .hasContentType("application/json")
            .time();
    return response;

}


public GetResponseCategoryDTO getCategoryById(int id,SoftAssert softAssert){
Response response=api.getCategoryClient().getCategoryById(id);


    assertionManager.getValidator().ValidateJson(response, GetResponseCategoryDTO.class,softAssert);
    assertionManager.getAssert().assertThat(response)
            .hasStatusCode(HttpStatusCode.OK)
            .hasContentType("application/json")
            .time();

        return response.as(GetResponseCategoryDTO.class);


}

    public DeleteCategoryResponseDto getCategoryByIdAfterDelete(int id,SoftAssert softAssert){
        Response response=api.getCategoryClient().getCategoryById(id);

        assertionManager.getValidator().ValidateJson(response,DeleteCategoryResponseDto.class,softAssert);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .time();

        return response.as(DeleteCategoryResponseDto.class);


    }
    public BadRequestResponse getCategoryByWrongId(int id,SoftAssert softAssert){
        Response response=api.getCategoryClient().getCategoryById(id);

        assertionManager.getValidator().ValidateJson(response,BadRequestResponse.class,softAssert);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();
        return response.as(BadRequestResponse.class);


    }

    public GetResponseCategoryDTO putCategoryById(int id, Object body,SoftAssert softAssert){

        Response response = api.getCategoryClient().putCategoryById(id, body);

        assertionManager.getValidator().ValidateJson(response,GetResponseCategoryDTO.class,softAssert);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .hasContentType("application/json")
                .time();

        return response.as(GetResponseCategoryDTO.class);

    }

    public PutBadRequestResponse putCategoryByIdBadRequest(int id, Object body,SoftAssert softAssert) {
        Response response = api.getCategoryClient().putCategoryById(id, body);

        assertionManager.getValidator().ValidateJson(response,PutBadRequestResponse.class,softAssert);

        assertionManager.getAssert().assertThat(response).hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .time();

        return response.as(PutBadRequestResponse.class);

    }

    public Response deleteCategory(int id){
        Response response =  api.getCategoryClient().deleteCategoryById(id);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .time();
        return response;
    }

    public BadRequestResponse deleteWithWrongCategoryId(int id,SoftAssert softAssert){
        Response response =  api.getCategoryClient().deleteCategoryById(id);

        assertionManager.getValidator().ValidateJson(response,BadRequestResponse.class,softAssert);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .time();
        return response.as(BadRequestResponse.class);
    }



public GetResponseCategoryDTO getCategoryWithSlug(String slug, SoftAssert softAssert){
    Response response =  api.getCategoryClient().getCategoryWithSlug(slug);

    assertionManager.getValidator().ValidateJson(response,GetResponseCategoryDTO.class,softAssert);
    assertionManager.getAssert().assertThat(response)
            .hasStatusCode(HttpStatusCode.OK)
            .hasContentType("application/json")
            .time();
    return response.as(GetResponseCategoryDTO.class);
}

    public BadRequestResponse getCategoryWithWrongSlug(String slug, SoftAssert softAssert){
        Response response =  api.getCategoryClient().getCategoryWithSlug(slug);

        assertionManager.getValidator().ValidateJson(response,BadRequestResponse.class,softAssert);
        assertionManager.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                  .time();
        return response.as(BadRequestResponse.class);
    }

    }
