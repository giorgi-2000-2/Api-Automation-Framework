package org.example.Steps;
import io.restassured.response.Response;
import org.example.ApiService.HttpStatusCode;
import org.example.DTOs.RequestDto.CreateCategoryRequestDto;
import org.example.DTOs.RequestDto.GetCategoryLimitRequestDTO;
import org.example.DTOs.ResponseDto.BadRequestResponse;
import org.example.DTOs.ResponseDto.GetResponseCategoryDTO;
import org.example.DTOs.ResponseDto.DeleteCategoryResponseDto;
import org.example.Managers.ObjectManager;

public class CategorySteps {
    private ObjectManager api;
    public CategorySteps(ObjectManager api) {
        this.api = api;
    }

    public GetResponseCategoryDTO createCategorySuccessfully(CreateCategoryRequestDto requestBody) {
        Response response = api.getCategoryClient().createCategory(requestBody);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.CREATED)
                .hasContentType("application/json")
                .time();

        return response.as(GetResponseCategoryDTO.class);

    }


    public void createCategoryBadRequest(CreateCategoryRequestDto requestBody) {
        Response response = api.getCategoryClient().createCategory(requestBody);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();
    }

public Response getCategoriesByLimit(GetCategoryLimitRequestDTO limit ) {
    Response response= api.getCategoryClient().getCategoryLimit(limit);

    api.getAssert().assertThat(response)
            .hasStatusCode(HttpStatusCode.OK)
            .hasContentType("application/json")
            .time();
    return response;

}


public GetResponseCategoryDTO getCategoryById(int id){
Response response=api.getCategoryClient().getCategoryById(id);
    api.getAssert().assertThat(response)
            .hasStatusCode(HttpStatusCode.OK)
            .hasContentType("application/json")
            .time();

        return response.as(GetResponseCategoryDTO.class);


}

    public DeleteCategoryResponseDto getCategoryByIdAfterDelete(int id){
        Response response=api.getCategoryClient().getCategoryById(id);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .time();

        return response.as(DeleteCategoryResponseDto.class);


    }
    public BadRequestResponse getCategoryByWrongId(int id){
        Response response=api.getCategoryClient().getCategoryById(id);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                .time();
        return response.as(BadRequestResponse.class);


    }

    public GetResponseCategoryDTO putCategoryById(int id, Object body){

        Response response = api.getCategoryClient().putCategoryById(id, body);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .hasContentType("application/json")
                .time();

        return response.as(GetResponseCategoryDTO.class);

    }

    public BadRequestResponse putCategoryByIdBadRequest(int id, Object body) {
        Response response = api.getCategoryClient().putCategoryById(id, body);
        api.getAssert().assertThat(response).hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .time();

        return response.as(BadRequestResponse.class);

    }

    public Response deleteCategory(int id){
        Response response =  api.getCategoryClient().deleteCategoryById(id);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.OK)
                .time();
        return response;
    }

    public BadRequestResponse deleteWithWrongCategoryId(int id){
        Response response =  api.getCategoryClient().deleteCategoryById(id);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .time();
        return response.as(BadRequestResponse.class);
    }



public GetResponseCategoryDTO getCategoryWIthSlug(String slug){
    Response response =  api.getCategoryClient().getCategoryWithSlug(slug);
    api.getAssert().assertThat(response)
            .hasStatusCode(HttpStatusCode.OK)
            .hasContentType("application/json")
            .time();
    return response.as(GetResponseCategoryDTO.class);
}

    public BadRequestResponse getCategoryWIthWrongSlug(String slug){
        Response response =  api.getCategoryClient().getCategoryWithSlug(slug);
        api.getAssert().assertThat(response)
                .hasStatusCode(HttpStatusCode.BAD_REQUEST)
                .hasContentType("application/json")
                  .time();
        return response.as(BadRequestResponse.class);
    }

    }
