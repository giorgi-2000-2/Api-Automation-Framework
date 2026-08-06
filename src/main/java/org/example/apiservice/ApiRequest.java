package org.example.apiservice;
import io.restassured.response.Response;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class ApiRequest {



    public Response getWithQueryParams(String endpoint, Map<String, ?> queryParams) {
        return given()
                .spec(ApiConfig.base())
                .queryParams(queryParams)
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
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }


    public Response post(String endpoint, Object body) {
        return given()
                .spec(ApiConfig.base())
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
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }



}
