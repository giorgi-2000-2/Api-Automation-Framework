package ge.gmikeladze.platzi.steps;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import io.restassured.response.Response;
import ge.gmikeladze.platzi.di.TestScoped;
import ge.gmikeladze.platzi.apiclient.ApiEndpoint;
import ge.gmikeladze.platzi.apiclient.GenericClient;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;

import java.util.List;
import java.util.Map;


@TestScoped
public class CategorySteps extends BaseSteps {
    private final GenericClient genericClient;

    @Inject
    public CategorySteps(GenericClient genericClient, ResponseValidator validator) {
        super(validator);
        this.genericClient = genericClient;
    }

    public GetResponseCategoryDto createCategory(CreateCategoryRequestDto body) {
        return createCategory(body, HttpStatusCode.CREATED);
    }

    public GetResponseCategoryDto createCategory(CreateCategoryRequestDto body,
                                                 HttpStatusCode expectedStatus) {
        step("კატეგორიის შექმნა");
        return validator.validate(
                genericClient.create(ApiEndpoint.CATEGORY, body),
                expectedStatus, GetResponseCategoryDto.class);
    }

    public <T> T createCategoryExpectingError(CreateCategoryRequestDto body,
                                              HttpStatusCode expectedStatus,
                                              Class<T> errorDto) {
        step("კატეგორიის შექმნის მცდელობა არავალიდური მონაცემებით");
        return validator.validate(
                genericClient.create(ApiEndpoint.CATEGORY, body),
                expectedStatus, errorDto);
    }

    public List<GetResponseCategoryDto> getCategories(int limit) {
        step("კატეგორიების სია limit=" + limit);
        return validator.validateList(
                genericClient.getByQuery(ApiEndpoint.CATEGORY, Map.of("limit", limit)),
                HttpStatusCode.OK,
                GetResponseCategoryDto[].class);
    }
    public List<GetResponseProductDto> getProductsByCategoryIdWithPagination(int id, int limit, int offset, HttpStatusCode expectedStatus) {
        return validator.validateList(
                genericClient.getByPathAndQuery(ApiEndpoint.CATEGORY_ID_PRODUCTS, id, limit, offset),
                expectedStatus,
                GetResponseProductDto[].class
        );
    }

    public GetResponseCategoryDto getCategoryById(int id) {
        return validator.validate(
                genericClient.getByPath(ApiEndpoint.CATEGORY_ID, Map.of("id",id)),
                HttpStatusCode.OK, GetResponseCategoryDto.class);
    }

    public GetResponseCategoryDto getCategoryBySlug(String slug) {
        return validator.validate(
                genericClient.getByPath(ApiEndpoint.CATEGORY_SLUG, Map.of("slug", slug)),
                HttpStatusCode.OK, GetResponseCategoryDto.class);
    }

    public <T> T getCategoryExpectingError(int id,
                                           HttpStatusCode expectedStatus,
                                           Class<T> errorDto) {
        return validator.validate(
                genericClient.getByPath(ApiEndpoint.CATEGORY_ID, Map.of("id",id)),
                expectedStatus, errorDto);
    }

    public <T> T getCategoryBySlugExpectingError(String slug,
                                                 HttpStatusCode expectedStatus,
                                                 Class<T> errorDto) {
        return validator.validate(
                genericClient.getByPath(ApiEndpoint.CATEGORY_SLUG, Map.of("slug", slug)),
                expectedStatus, errorDto);
    }

    public GetResponseCategoryDto updateCategory(int id, UpdateCategoryRequestDto body) {
        step("კატეგორიის განახლება id=" + id);
        return validator.validate(
                genericClient.update(ApiEndpoint.CATEGORY_ID, id, body),
                HttpStatusCode.OK, GetResponseCategoryDto.class);
    }

    public <T> T updateCategoryExpectingError(int id,
                                              Object body,
                                              HttpStatusCode expectedStatus,
                                              Class<T> errorDto) {
        step("კატეგორიის განახლების მცდელობა არავალიდური მონაცემებით, id=" + id);
        return validator.validate(
                genericClient.update(ApiEndpoint.CATEGORY_ID, id, body),
                expectedStatus, errorDto);
    }

    public boolean deleteCategory(int id) {
        step("კატეგორიის წაშლა id=" + id);
        Response response = validator.validateWithoutSchema(
                genericClient.delete(ApiEndpoint.CATEGORY_ID, id), HttpStatusCode.OK);
        return Boolean.parseBoolean(response.asString().trim());
    }

    public Response deleteCategoryById(int id) {
        step("კატეგორიის წაშლა id=" + id);
        Response response = validator.validateWithoutSchema(
                genericClient.delete(ApiEndpoint.CATEGORY_ID, id), HttpStatusCode.OK);
        return response;
    }


    public <T> T deleteCategoryExpectingError(int id,
                                              HttpStatusCode expectedStatus,
                                              Class<T> errorDto) {
        return validator.validate(
                genericClient.delete(ApiEndpoint.CATEGORY_ID, id),
                expectedStatus, errorDto);
    }


}