package ge.gmikeladze.platzi.tests;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.negative.NegativeCase;
import ge.gmikeladze.platzi.datafactories.negative.ProductNegativeData;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.error.ApiError;
import ge.gmikeladze.platzi.dtos.response.error.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.function.IntFunction;

public class ProductTest extends BaseApiTest {


    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    public void testCreateProductValidData() {
        CreateProductRequestDto requestBody = productData.createProductWithData(context.get().getCategory().getId());
        GetResponseProductDto response = productSteps.get().create(requestBody);
        productAssert.get().assertThat(response)
                .hasTitle(requestBody.getTitle())
                .hasPrice(requestBody.getPrice())
                .hasDescription(requestBody.getDescription())
                .hasCategoryId(requestBody.getCategoryId())
                .hasImages(requestBody.getImages());
    }

    @RequiresCategory
    @RequiresProduct
    @Test(groups = {"smoke", "regression","positive"})
    public void testGetProductById() {
        GetResponseProductDto response = productSteps.get().getById(context.get().getProduct().getId());
        productAssert.get().assertThat(response)
                .hasTitle(context.get().getProductRequest().getTitle())
                .hasPrice(context.get().getProductRequest().getPrice())
                .hasDescription(context.get().getProductRequest().getDescription())
                .hasCategoryId(context.get().getProductRequest().getCategoryId())
                .hasImages(context.get().getProductRequest().getImages());
    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    @RequiresProduct
    public void testUpdateProductSuccessfully() {

        UpdateProductRequestDto requests = productData.updateProductDto(context.get().getCategory().getId());
        GetResponseProductDto response = productSteps.get().update(context.get().getProduct().getId(), requests);
        context.get().setProduct(response);
        productAssert.get().assertThat(response)
                .hasTitle(requests.getTitle())
                .hasPrice(requests.getPrice())
                .hasDescription(requests.getDescription())
                .hasCategoryId(requests.getCategoryId())
                .hasImages(requests.getImages());

    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    @RequiresProduct
    public void testDeleteProductSuccessfully() {
        Response response = productSteps.get().delete(context.get().getProduct().getId());
        productAssert.get().assertThat(response)
                .isDeletedSuccessfully();
        productSteps.get().deleteExpectingError(context.get().getProduct().getId(),
                HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
    }



    @Test(groups = {"regression","negative"},
            dataProvider = "invalidProductCreate", dataProviderClass = ProductNegativeData.class)
    @RequiresCategory
    public void testCreateProductNegative(NegativeCase<IntFunction<CreateProductRequestDto>> testCase) {
        CreateProductRequestDto requestBody = testCase.getPayload().apply(context.get().getCategory().getId());
        ApiError error = productSteps.get().createExpectingError(
                requestBody, testCase.getExpectedStatus(), testCase.getErrorDto());
        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidProductId", dataProviderClass = ProductNegativeData.class)
    public void testGetProductByIdNegative(NegativeCase<Integer> testCase) {
        ApiError error = productSteps.get().getExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidProductId", dataProviderClass = ProductNegativeData.class)

    public void testDeleteProductNegative(NegativeCase<Integer> testCase) {
        ApiError error = productSteps.get().deleteExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidProductUpdate", dataProviderClass = ProductNegativeData.class)
    @RequiresCategory
    @RequiresProduct
    public void testUpdateProductNegative(NegativeCase<IntFunction<UpdateProductRequestDto>> testCase) {

        UpdateProductRequestDto requestBody = testCase.getPayload().apply(context.get().getCategory().getId());

        ApiError error = productSteps.get().updateExpectingError(
                context.get().getProduct().getId(), requestBody,
                testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());

    }
}