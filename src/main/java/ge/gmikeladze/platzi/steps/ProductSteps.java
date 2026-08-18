package ge.gmikeladze.platzi.steps;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiclient.ApiEndpoint;
import ge.gmikeladze.platzi.apiclient.GenericClient;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.cleanup.ResourceKey;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

@TestScoped
public class ProductSteps extends AbstractResourceSteps<CreateProductRequestDto, GetResponseProductDto, UpdateProductRequestDto> {
    private final GenericClient genericClient;

    @Inject
    public ProductSteps(GenericClient genericClient,
                        ResponseValidator validator,
                        TestContext testContext, GenericClient genericClient1, TestContext testContext1) {
        super(genericClient, validator, testContext);
        this.genericClient = genericClient1;
    }

    @Override
    protected ApiEndpoint collectionEndpoint() {
        return ApiEndpoint.PRODUCT;
    }

    @Override
    protected ApiEndpoint itemEndpoint() {
        return ApiEndpoint.PRODUCT_ID;
    }

    @Override
    protected Class<GetResponseProductDto> responseType() {
        return GetResponseProductDto.class;
    }

    @Override
    protected String resourceType() {
        return ResourceKey.TYPE_PRODUCT;
    }

    @Override
    protected void bestEffortDelete(int id) {
        Response response = genericClient.delete(ApiEndpoint.PRODUCT_ID, id);
        if (response.statusCode() != HttpStatusCode.OK.getCode()) {
            logBestEffortFailure(id, response.statusCode());
        }
    }


    public List<GetResponseProductDto> getProductsByCategoryId(Integer categoryId, HttpStatusCode expectedStatus) {
        Response response = genericClient.getByPath(
                ApiEndpoint.CATEGORY_ID_PRODUCTS,
                Map.of("id", categoryId)
        );

        return validator.validateList(response, expectedStatus, GetResponseProductDto[].class);
    }






}