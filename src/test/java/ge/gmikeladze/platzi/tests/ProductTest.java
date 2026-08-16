package ge.gmikeladze.platzi.tests;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.NegativeCase;
import ge.gmikeladze.platzi.datafactories.ProductNegativeData;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.ApiError;
import ge.gmikeladze.platzi.dtos.response.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.function.IntFunction;

public class ProductTest extends BaseApiTest {


    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    public void testCreateProductValidData() {
        CreateProductRequestDto requestBody = productData.createProductWithData(context.get().getCategory().getId());
        GetResponseProductDto response = productSteps.get().createProduct(requestBody);
        productAssert.get().assertThat(response)
                .verifyTitleIsCorrect(requestBody.getTitle())
                .verifyPriceIsCorrect(requestBody.getPrice())
                .verifyDescriptionIsCorrect(requestBody.getDescription())
                .verifyCategoryIdIsCorrect(requestBody.getCategoryId())
                .verifyImagesAreCorrect(requestBody.getImages());
    }

    @RequiresCategory
    @RequiresProduct
    @Test(groups = {"smoke", "regression","positive"})
    public void testGetProductById() {
        GetResponseProductDto response = productSteps.get().getProduct(context.get().getProduct().getId());
        productAssert.get().assertThat(response)
                .verifyTitleIsCorrect(context.get().getProductRequest().getTitle())
                .verifyPriceIsCorrect(context.get().getProductRequest().getPrice())
                .verifyDescriptionIsCorrect(context.get().getProductRequest().getDescription())
                .verifyCategoryIdIsCorrect(context.get().getProductRequest().getCategoryId())
                .verifyImagesAreCorrect(context.get().getProductRequest().getImages());
    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    @RequiresProduct
    public void testUpdateProductSuccessfully() {

        UpdateProductRequestDto requests = productData.updateProductDto(context.get().getCategory().getId());
        GetResponseProductDto response = productSteps.get().updateProduct(context.get().getProduct().getId(), requests);
        context.get().setProduct(response);
        productAssert.get().assertThat(response)
                .verifyTitleIsCorrect(requests.getTitle())
                .verifyPriceIsCorrect(requests.getPrice())
                .verifyDescriptionIsCorrect(requests.getDescription())
                .verifyCategoryIdIsCorrect(requests.getCategoryId())
                .verifyImagesAreCorrect(requests.getImages());

    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    @RequiresProduct
    public void testDeleteProductSuccessfully() {
        Response response = productSteps.get().deleteProduct(context.get().getProduct().getId());
        productAssert.get().assertThat(response)
                .verifyBooleanResponseIsCorrect();
        productSteps.get().deleteProductExpectingError(context.get().getProduct().getId(),
                HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
    }



    @Test(groups = {"regression","negative"},
            dataProvider = "invalidProductCreate", dataProviderClass = ProductNegativeData.class)
    @RequiresCategory
    public void testCreateProductNegative(NegativeCase<IntFunction<CreateProductRequestDto>> testCase) {

        CreateProductRequestDto requestBody = testCase.getPayload().apply(context.get().getCategory().getId());

        ApiError error = productSteps.get().createProductExpectingError(
                requestBody, testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidProductId", dataProviderClass = ProductNegativeData.class)
    public void testGetProductByIdNegative(NegativeCase<Integer> testCase) {
        ApiError error = productSteps.get().getProductExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidProductId", dataProviderClass = ProductNegativeData.class)

    public void testDeleteProductNegative(NegativeCase<Integer> testCase) {
        ApiError error = productSteps.get().deleteProductExpectingError(
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

        ApiError error = productSteps.get().updateProductExpectingError(
                context.get().getProduct().getId(), requestBody,
                testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());

    }
}