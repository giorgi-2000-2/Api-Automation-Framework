package org.example.Steps;

import io.restassured.response.Response;
import org.example.ApiClient.GenericClient;
import org.example.ApiService.HttpStatusCode;
import org.example.AssertionManager.ResponseValidator;
import org.example.DTOs.RequestDto.CreateCategoryRequestDto;
import org.example.DTOs.RequestDto.UpdateCategoryRequestDto;
import org.example.DTOs.ResponseDto.GetResponseCategoryDto;

import java.util.List;
import java.util.Map;

public class CategorySteps extends BaseSteps {

    private final GenericClient genericClient;

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
        return validator.validateSuccess(
                genericClient.create(ApiEndpoint.CATEGORY, body),
                expectedStatus, GetResponseCategoryDto.class);
    }

    public <T> T createCategoryExpectingError(CreateCategoryRequestDto body,
                                              HttpStatusCode expectedStatus,
                                              Class<T> errorDto) {
        step("კატეგორიის შექმნის მცდელობა არავალიდური მონაცემებით");
        return validator.validateSuccess(
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

    public GetResponseCategoryDto getCategory(int id) {
        return validator.validateSuccess(
                genericClient.getById(ApiEndpoint.CATEGORY_ID, id),
                HttpStatusCode.OK, GetResponseCategoryDto.class);
    }

    public GetResponseCategoryDto getCategoryBySlug(String slug) {
        return validator.validateSuccess(
                genericClient.getByPath(ApiEndpoint.CATEGORY_SLUG, Map.of("slug", slug)),
                HttpStatusCode.OK, GetResponseCategoryDto.class);
    }

    public <T> T getCategoryExpectingError(int id,
                                           HttpStatusCode expectedStatus,
                                           Class<T> errorDto) {
        return validator.validateSuccess(
                genericClient.getById(ApiEndpoint.CATEGORY_ID, id),
                expectedStatus, errorDto);
    }

    public <T> T getCategoryBySlugExpectingError(String slug,
                                                 HttpStatusCode expectedStatus,
                                                 Class<T> errorDto) {
        return validator.validateSuccess(
                genericClient.getByPath(ApiEndpoint.CATEGORY_SLUG, Map.of("slug", slug)),
                expectedStatus, errorDto);
    }

    public GetResponseCategoryDto updateCategory(int id, UpdateCategoryRequestDto body) {
        step("კატეგორიის განახლება id=" + id);
        return validator.validateSuccess(
                genericClient.update(ApiEndpoint.CATEGORY_ID, id, body),
                HttpStatusCode.OK, GetResponseCategoryDto.class);
    }

    public <T> T updateCategoryExpectingError(int id,
                                              Object body,
                                              HttpStatusCode expectedStatus,
                                              Class<T> errorDto) {
        step("კატეგორიის განახლების მცდელობა არავალიდური მონაცემებით, id=" + id);
        return validator.validateSuccess(
                genericClient.update(ApiEndpoint.CATEGORY_ID, id, body),
                expectedStatus, errorDto);
    }

    public boolean deleteCategory(int id) {
        step("კატეგორიის წაშლა id=" + id);
        Response response = validator.validateWithoutSchema(
                genericClient.delete(ApiEndpoint.CATEGORY_ID, id), HttpStatusCode.OK);
        return Boolean.parseBoolean(response.asString().trim());
    }

    public <T> T deleteCategoryExpectingError(int id,
                                              HttpStatusCode expectedStatus,
                                              Class<T> errorDto) {
        return validator.validateSuccess(
                genericClient.delete(ApiEndpoint.CATEGORY_ID, id),
                expectedStatus, errorDto);
    }
}