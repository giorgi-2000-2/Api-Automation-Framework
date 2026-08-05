package org.example.ApiClient;

import io.restassured.response.Response;
import org.example.ApiService.ApiRequest;
import org.example.Steps.ApiEndpoint;

import java.util.Map;

public class GenericClient {
    private final ApiRequest apiRequest;

    public GenericClient(ApiRequest apiRequest) {
        this.apiRequest = apiRequest;
    }

    public Response create(ApiEndpoint endpoint, Object body) {
        return apiRequest.post(endpoint.path(), body);
    }

    public Response getByPath(ApiEndpoint endpoint, Map<String, ?> pathParams) {
        return apiRequest.getWithPathParam(endpoint.path(), pathParams);
    }

    public Response getByQuery(ApiEndpoint endpoint, Map<String, ?> queryParams) {
        return apiRequest.getWithQueryParams(endpoint.path(), queryParams);
    }

    public Response update(ApiEndpoint endpoint, Map<String, ?> pathParams, Object body) {
        return apiRequest.put(endpoint.path(), pathParams, body);
    }

    public Response delete(ApiEndpoint endpoint, int id) {
        return apiRequest.delete(endpoint.path(), id);
    }

    public Response getById(ApiEndpoint endpoint, int id) {
        return getByPath(endpoint, Map.of("id", id));
    }

    public Response update(ApiEndpoint endpoint, int id, Object body) {
        return update(endpoint, Map.of("id", id), body);
    }
}



