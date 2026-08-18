package ge.gmikeladze.platzi.steps;

import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiclient.ApiEndpoint;
import ge.gmikeladze.platzi.apiclient.GenericClient;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.cleanup.ResourceKey;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

@TestScoped
public class CategorySteps extends AbstractResourceSteps<CreateCategoryRequestDto, GetResponseCategoryDto, UpdateCategoryRequestDto> {

    @Inject
    public CategorySteps(GenericClient genericClient,
                         ResponseValidator validator,
                         TestContext testContext) {
        super(genericClient, validator, testContext);
    }

    @Override
    protected ApiEndpoint collectionEndpoint() {
        return ApiEndpoint.CATEGORY;
    }

    @Override
    protected ApiEndpoint itemEndpoint() {
        return ApiEndpoint.CATEGORY_ID;
    }

    @Override
    protected Class<GetResponseCategoryDto> responseType() {
        return GetResponseCategoryDto.class;
    }

    @Override
    protected String resourceType() {
        return ResourceKey.TYPE_CATEGORY;
    }

    @Override
    protected void bestEffortDelete(int id) {
        Response response = genericClient.delete(ApiEndpoint.CATEGORY_ID, id);
        if (response.statusCode() != HttpStatusCode.OK.getCode()) {
            logBestEffortFailure(id, response.statusCode());
        }
    }

    public List<GetResponseCategoryDto> getCategories(int limit) {
        step("კატეგორიების სია limit=" + limit);
        return validator.validateList(
                genericClient.getByQuery(ApiEndpoint.CATEGORY, Map.of("limit", limit)),
                HttpStatusCode.OK,
                GetResponseCategoryDto[].class
        );
    }

    public GetResponseCategoryDto getCategoryBySlug(String slug) {
        step("კატეგორიის წამოღება slug-ით: " + slug);
        return validator.validate(
                genericClient.getByPath(ApiEndpoint.CATEGORY_SLUG, Map.of("slug", slug)),
                HttpStatusCode.OK,
                GetResponseCategoryDto.class
        );
    }

    public <T> T getCategoryBySlugExpectingError(String slug,
                                                 HttpStatusCode expectedStatus,
                                                 Class<T> errorDto) {
        return validator.validate(
                genericClient.getByPath(ApiEndpoint.CATEGORY_SLUG, Map.of("slug", slug)),
                expectedStatus,
                errorDto
        );
    }

    public List<GetResponseProductDto> getProductsByCategoryIdWithPagination(
            int id, int limit, int offset, HttpStatusCode expectedStatus) {
        step("კატეგორიის პროდუქტების სია id=" + id + ", limit=" + limit + ", offset=" + offset);
        return validator.validateList(
                genericClient.getByPathAndQuery(ApiEndpoint.CATEGORY_ID_PRODUCTS, id, limit, offset),
                expectedStatus,
                GetResponseProductDto[].class
        );
    }
}