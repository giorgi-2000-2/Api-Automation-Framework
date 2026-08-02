package org.example.ApiClient;

import io.restassured.response.Response;
import org.example.DTOs.RequestDto.CreateProductRequestDto;
import org.example.Managers.ObjectManager;
import org.example.Utils.ApiConfig;
import org.example.Utils.ConfigReader;

import java.util.Map;

public class ProductApiClient {
    private final ObjectManager api;

    public ProductApiClient(ObjectManager api) {
        this.api = api;
    }
    public Response createProduct(CreateProductRequestDto requestBody) {
        return api.getRequest().post(ConfigReader.get("product.endpoint"), ApiConfig.getBaseSpec(), requestBody);
    }

    public Response getProductById(int id){
        Map<String, Integer> queryParam = Map.of("id", id);
        return api.getRequest().getWithPathParam(ConfigReader.get("product.id.endpoint"), ApiConfig.getBaseSpec(), queryParam);

    }
    public Response putProductById(int id,Object body){
        Map<String, Integer> queryParam = Map.of("id", id);
        return api.getRequest().put(ConfigReader.get("product.id.endpoint"), ApiConfig.getBaseSpec(), queryParam, body);

    }
    public Response deleteProductById(int id){
        return api.getRequest().delete(ConfigReader.get("product.id.endpoint"),id);

    }
}
