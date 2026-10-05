package ge.gmikeladze.platzi.apiservice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import static io.restassured.RestAssured.given;

@Singleton
public class ApiRequest {

    private final RequestSpecification spec;

    @Inject
    public ApiRequest(RequestSpecification spec) {
        this.spec = spec;
    }

    public Response post(String endpoint, Object body) {
        return given()
                .spec(spec)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response get(String endpoint, Map<String, ?> pathParams, Map<String, ?> queryParams) {
        return given()
                .spec(spec)
                .pathParams(pathParams)
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response put(String endpoint, Map<String, ?> pathParams, Object body) {
        return given()
                .spec(spec)
                .pathParams(pathParams)
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response delete(String endpoint, Map<String, ?> pathParams) {
        return given()
                .spec(spec)
                .pathParams(pathParams)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response getWithAuth(String endpoint, String accessToken) {
        return given()
                .spec(spec)
                .header("Authorization", "Bearer " + (accessToken != null ? accessToken : ""))
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response getWithoutAuth(String endpoint) {
        return given()
                .spec(spec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }


}