package org.example.ApiService;
import io.restassured.response.Response;
import org.example.Utils.LogFilter;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class ApiRequest {



    public Response getWithQueryParams(String endpoint, Map<String, ?> queryParams) {
        return given()
                .spec(ApiConfig.base())
                .queryParams(queryParams)
                .filter(new LogFilter())
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response getWithPathParam(String endpoint, Map<String, ?> patchParam) {
        return given()
                .spec(ApiConfig.base())
                .pathParams(patchParam)
                .filter(new LogFilter())
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }


    public Response post(String endpoint, Object body) {
        return given()
                .spec(ApiConfig.base())
                .filter(new LogFilter())
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }



    public Response put(String endpoint, Map<String, ?> pathParam, Object body) {
        return given()
                .spec(ApiConfig.base())
                .pathParams(pathParam)
                .filter(new LogFilter())
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response delete(String endpoint, int id) {
        return given()
                .pathParam("id", id)
                .spec(ApiConfig.base())
                .filter(new LogFilter())
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }



}
