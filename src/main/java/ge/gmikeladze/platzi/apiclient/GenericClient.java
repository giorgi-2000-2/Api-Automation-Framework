package ge.gmikeladze.platzi.apiclient;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import io.restassured.response.Response;
import ge.gmikeladze.platzi.apiservice.ApiRequest;

import java.util.List;
import java.util.Map;

@Singleton
public class GenericClient {
    private final ApiRequest apiRequest;

    @Inject
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

    public Response delete(ApiEndpoint endpoint, int id) {
        return apiRequest.delete(endpoint.path(), id);
    }

    public Response update(ApiEndpoint endpoint, int id, Object body) {
        return apiRequest.put(endpoint.path(), Map.of("id", id), body);
    }
    public Response getByPathAndQuery(ApiEndpoint endpoint, int id, int limit, int offset) {
        return apiRequest.getProductsByCategoryIdWithPagination(endpoint.path(), id, limit, offset);
    }




}



