package org.example.ApiService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.Managers.ObjectManager;
import org.example.Utils.ConfigReader;
import org.example.Utils.LogFilter;

import java.util.Map;
import static io.restassured.RestAssured.given;

public class ApiRequest {
    private ObjectManager api;
    public ApiRequest(ObjectManager api) {
        this.api = api;

    }


    public Response getWithQueryParams(String endpoint, RequestSpecification baseSpec, Map<String, ?> queryParams) {
        return given()
                .spec(baseSpec)
                .queryParams(queryParams)
                .log().all()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response getWithPathParam(String endpoint, RequestSpecification baseSpec, Map<String, ?> patchParam) {
        return given()
                .spec(baseSpec)
                .pathParams(patchParam)
                .log().all()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }


    public Response post(String endpoint, RequestSpecification baseSpec, Object body) {
        return given()
                .spec(baseSpec)
                .body(body)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }



    public Response put(String endpoint, RequestSpecification baseSpec, Map<String, ?> pathParam, Object body) {
        return given()
                .spec(baseSpec)
                .pathParams(pathParam)
                .body(body)
                .log().all()
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response delete(String endpoint, RequestSpecification baseSpec, int id) {
        return given()
                .pathParam("id", id)
                .spec(baseSpec)
                .log().all()
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }

    public Response delete(String endpoint, int id) {
        return given()
                .pathParam("id", id)
                .baseUri(ConfigReader.get("BASE_URL"))
                .accept(ContentType.ANY)
                .filter(new LogFilter())
                .log().all()
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .extract()
                .response();
    }


}
