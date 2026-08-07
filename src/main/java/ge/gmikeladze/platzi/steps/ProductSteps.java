package ge.gmikeladze.platzi.steps;
import com.google.inject.Inject;
import io.restassured.response.Response;
import ge.gmikeladze.platzi.apiclient.ApiEndpoint;
import ge.gmikeladze.platzi.apiclient.GenericClient;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;

public class ProductSteps extends BaseSteps {
    private final GenericClient genericClient;
    @Inject
    public ProductSteps(GenericClient genericClient, ResponseValidator validator) {
        super(validator);
        this.genericClient = genericClient;
    }

    public GetResponseProductDto createProduct(CreateProductRequestDto body) {
        return createProduct(body, HttpStatusCode.CREATED);
    }

    public GetResponseProductDto createProduct(CreateProductRequestDto body,
                                               HttpStatusCode expectedStatus) {
        step("პროდუქტის შექმნა");
        return validator.validate(
                genericClient.create(ApiEndpoint.PRODUCT, body),
                expectedStatus, GetResponseProductDto.class);
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
                genericClient.getById(ApiEndpoint.PRODUCT_ID, id),
                HttpStatusCode.OK, GetResponseProductDto.class);
    }

    public <T> T getProductExpectingError(int id,
                                          HttpStatusCode expectedStatus,
                                          Class<T> errorDto) {
        return validator.validate(
                genericClient.getById(ApiEndpoint.PRODUCT_ID, id),
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
        return validator.validateWithoutSchema(
                genericClient.delete(ApiEndpoint.PRODUCT_ID, id), HttpStatusCode.OK);
    }

    public <T> T deleteProductExpectingError(int id,
                                             HttpStatusCode expectedStatus,
                                             Class<T> errorDto) {
        return validator.validate(
                genericClient.delete(ApiEndpoint.PRODUCT_ID, id),
                expectedStatus, errorDto);
    }
}