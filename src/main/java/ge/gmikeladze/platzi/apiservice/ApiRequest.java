package ge.gmikeladze.platzi.apiservice;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
@Singleton
public class ApiRequest {
    private RequestSpecification spec;

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

    public Response getWithQueryParams(String endpoint, Map<String, ?> queryParams) {
        return given()
                .spec(spec)
                .queryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response getWithPathParam(String endpoint, Map<String, ?> patchParam) {
        return given()
                .spec(spec)
                .pathParams(patchParam)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }


    public Response put(String endpoint, Map<String, ?> pathParam, Object body) {
        return given()
                .spec(spec)
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
                .spec(spec)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    public Response getProductsByCategoryIdWithPagination(String endpoint,int categoryId, int limit,
                                                                             int offset) {
        return given()
                .pathParam("id", categoryId)
                .queryParam("limit", limit)
                .queryParam("offset", offset)
                .spec(spec)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

}
