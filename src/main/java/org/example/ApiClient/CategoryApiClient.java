package org.example.ApiClient;
import io.restassured.response.Response;
import org.example.DTOs.RequestDto.GetCategoryLimitRequestDto;
import org.example.Managers.ObjectManager;
import org.example.DTOs.RequestDto.CreateCategoryRequestDto;
import org.example.Utils.ApiConfig;
import org.example.Utils.ConfigReader;
import java.util.Map;

public class CategoryApiClient{

    private final ObjectManager api;

    public CategoryApiClient(ObjectManager api) {
        this.api = api;
    }

    public Response createCategory(CreateCategoryRequestDto requestBody) {
        return api.getRequest().post(ConfigReader.get("category.endpoint"), ApiConfig.getBaseSpec(), requestBody);
    }



    public Response getCategoryLimit(GetCategoryLimitRequestDto limit) {
        Map<String, Integer> queryParams = Map.of("limit", limit.getLimit());
        return api.getRequest().getWithQueryParams(ConfigReader.get("category.endpoint"), ApiConfig.getBaseSpec(), queryParams);
    }

    public Response getCategoryById(int id){
        Map<String, Integer> queryParam = Map.of("id", id);
        return api.getRequest().getWithPathParam(ConfigReader.get("category.by.id.endpoint"), ApiConfig.getBaseSpec(), queryParam);

    }

    public Response putCategoryById(int id,Object body){
        Map<String, Integer> queryParam = Map.of("id", id);
        return api.getRequest().put(ConfigReader.get("category.by.id.endpoint"), ApiConfig.getBaseSpec(), queryParam, body);

    }


    public Response deleteCategoryById(int id){
        return api.getRequest().delete(ConfigReader.get("category.by.id.endpoint"),id);

    }

    public Response getCategoryWithSlug(String slug){
        Map<String,String>queryParam = Map.of("slug",slug);
return api.getRequest().getWithPathParam(ConfigReader.get("category.by.slug.endpoint"), ApiConfig.getBaseSpec(), queryParam);
    }

}