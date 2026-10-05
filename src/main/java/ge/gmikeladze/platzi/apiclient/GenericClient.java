package ge.gmikeladze.platzi.apiclient;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.restassured.response.Response;
import ge.gmikeladze.platzi.apiservice.ApiRequest;
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

    public Response get(ApiEndpoint endpoint,
                        Map<String, ?> pathParams,
                        Map<String, ?> queryParams) {
        return apiRequest.get(endpoint.path(), pathParams, queryParams);
    }

    public Response getByPath(ApiEndpoint endpoint, Map<String, ?> pathParams) {
        return get(endpoint, pathParams, Map.of());
    }

    public Response getByQuery(ApiEndpoint endpoint, Map<String, ?> queryParams) {
        return get(endpoint, Map.of(), queryParams);
    }

    public Response update(ApiEndpoint endpoint, int id, Object body) {
        return apiRequest.put(endpoint.path(), Map.of("id", id), body);
    }

    public Response delete(ApiEndpoint endpoint, int id) {
        return apiRequest.delete(endpoint.path(), Map.of("id", id));
    }
    public Response post(ApiEndpoint endpoint, Object body) {
        return apiRequest.post(endpoint.path(), body);
    }

    public Response getWithAuth(ApiEndpoint endpoint, String accessToken) {
        return apiRequest.getWithAuth(endpoint.path(), accessToken);
    }
    public Response getWithoutAuth(ApiEndpoint endpoint) {
        return apiRequest.getWithoutAuth(endpoint.path());
    }
}


