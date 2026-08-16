package ge.gmikeladze.platzi.steps;

import com.aventstack.extentreports.Status;
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
import ge.gmikeladze.platzi.utils.ExtentReportManager;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

@TestScoped
public class ProductSteps extends BaseSteps {
    private final GenericClient genericClient;
    private final TestContext testContext;

    @Inject
    public ProductSteps(GenericClient genericClient,
                        ResponseValidator validator,
                        TestContext testContext) {
        super(validator);
        this.genericClient = genericClient;
        this.testContext = testContext;
    }

    public GetResponseProductDto createProduct(CreateProductRequestDto body) {
        return createProduct(body, HttpStatusCode.CREATED);
    }

    public GetResponseProductDto createProduct(CreateProductRequestDto body,
                                               HttpStatusCode expectedStatus) {
        step("პროდუქტის შექმნა");

        GetResponseProductDto product = validator.validate(
                genericClient.create(ApiEndpoint.PRODUCT, body),
                expectedStatus, GetResponseProductDto.class);

        if (product != null && product.getId() != null) {
            int id = product.getId();
            testContext.getCleanupRegistry()
                    .register(new ResourceKey(ResourceKey.TYPE_PRODUCT, id), () -> bestEffortDeleteProduct(id));
        }
        return product;
    }

    public <T> T createProductExpectingError(CreateProductRequestDto body,
                                             HttpStatusCode expectedStatus,
                                             Class<T> errorDto) {
        step("პროდუქტის შექმნის მცდელობა არავალიდური მონაცემებით");
        return validator.validate(
                genericClient.create(ApiEndpoint.PRODUCT, body),
                expectedStatus, errorDto);
    }

    public GetResponseProductDto getProduct(int id) {
        return validator.validate(
                genericClient.getByPath(ApiEndpoint.PRODUCT_ID, Map.of("id", id)),
                HttpStatusCode.OK, GetResponseProductDto.class);
    }

    public <T> T getProductExpectingError(int id,
                                          HttpStatusCode expectedStatus,
                                          Class<T> errorDto) {
        return validator.validate(
                genericClient.getByPath(ApiEndpoint.PRODUCT_ID, Map.of("id", id)),
                expectedStatus, errorDto);
    }

    public GetResponseProductDto updateProduct(int id, UpdateProductRequestDto body) {
        step("პროდუქტის განახლება id=" + id);
        return validator.validate(
                genericClient.update(ApiEndpoint.PRODUCT_ID, id, body),
                HttpStatusCode.OK, GetResponseProductDto.class);
    }

    public <T> T updateProductExpectingError(int id,
                                             Object body,
                                             HttpStatusCode expectedStatus,
                                             Class<T> errorDto) {
        step("პროდუქტის განახლების მცდელობა არავალიდური მონაცემებით, id=" + id);
        return validator.validate(
                genericClient.update(ApiEndpoint.PRODUCT_ID, id, body),
                expectedStatus, errorDto);
    }

    public Response deleteProduct(int id) {
        step("პროდუქტის წაშლა id=" + id);
        Response response = validator.validateWithoutSchema(
                genericClient.delete(ApiEndpoint.PRODUCT_ID, id), HttpStatusCode.OK);

        testContext.getCleanupRegistry().markCompleted(
                new ResourceKey(ResourceKey.TYPE_PRODUCT, id)
        );
        return response;
    }

    public <T> T deleteProductExpectingError(int id,
                                             HttpStatusCode expectedStatus,
                                             Class<T> errorDto) {
        return validator.validate(
                genericClient.delete(ApiEndpoint.PRODUCT_ID, id),
                expectedStatus, errorDto);
    }

    public List<GetResponseProductDto> getProductsByCategoryId(Integer categoryId, HttpStatusCode expectedStatus) {
        Response response = genericClient.getByPath(
                ApiEndpoint.CATEGORY_ID_PRODUCTS,
                Map.of("id", categoryId)
        );

        return validator.validateList(response, expectedStatus, GetResponseProductDto[].class);
    }

    private void bestEffortDeleteProduct(int id) {
        Response response = genericClient.delete(ApiEndpoint.PRODUCT_ID, id);
        if (response.statusCode() != HttpStatusCode.OK.getCode()) {
            ExtentReportManager.log(Status.WARNING,
                    "cleanup: პროდუქტი " + id + " ვერ წაიშალა (სტატუსი " + response.statusCode() + ")");
        }

    }




}