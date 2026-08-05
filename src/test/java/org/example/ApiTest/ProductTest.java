package org.example.ApiTest;
import io.restassured.response.Response;
import org.example.ApiService.HttpStatusCode;
import org.example.BaseApiTest;
import org.example.DTOs.RequestDto.CreateProductRequestDto;
import org.example.DTOs.RequestDto.UpdateProductRequestDto;
import org.example.DTOs.ResponseDto.BadRequestResponse;
import org.example.DTOs.ResponseDto.GetResponseProductDto;
import org.example.DTOs.ResponseDto.PutBadRequestResponse;
import org.example.annotations.RequiresCategory;
import org.example.annotations.RequiresProduct;
import org.testng.annotations.Test;

public class ProductTest extends BaseApiTest {

    @Test
    @RequiresCategory
    public void testCreateProductValidData() {
        CreateProductRequestDto requestBody = factory().productFactory().createProductWithData(category.get().getId());
        GetResponseProductDto response =  api().getProductSteps().createProduct(requestBody);
        assertManager().getResponseProductDtoAssert().assertThat(response)
                .verifyTitleIsCorrect(requestBody.getTitle())
                .verifyPriceIsCorrect(requestBody.getPrice())
                .verifyDescriptionIsCorrect(requestBody.getDescription())
                .verifyCategoryIdIsCorrect(requestBody.getCategoryId())
                .verifyImagesAreCorrect(requestBody.getImages());
    }

    @Test
    public void testCreateProductInvalidCategoryId() {
        CreateProductRequestDto requestBody = factory().productFactory().createProductWithData(factory().getRandomData().getWrongNumber());
       api().getProductSteps().createProductExpectingError(requestBody,HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);
    }


    @Test
    @RequiresCategory
    @RequiresProduct
    public void testGetProductById() {
      GetResponseProductDto response= api().getProductSteps().getProduct(product.get().getId());
        assertManager().getResponseProductDtoAssert().assertThat(response)
                .verifyTitleIsCorrect(requestBodyProduct.get().getTitle())
                .verifyPriceIsCorrect(requestBodyProduct.get().getPrice())
                .verifyDescriptionIsCorrect(requestBodyProduct.get().getDescription())
                .verifyCategoryIdIsCorrect(requestBodyProduct.get().getCategoryId())
                .verifyImagesAreCorrect(requestBodyProduct.get().getImages());
    }


    @Test
    public void testGetProductByWrongId() {
       api().getProductSteps().getProductExpectingError(factory().getRandomData().getWrongNumber(),HttpStatusCode.BAD_REQUEST,
               BadRequestResponse.class);
    }

    @Test
    @RequiresCategory
    @RequiresProduct
    public void testUpdateProductSuccessfully() {
        UpdateProductRequestDto requests = factory().productFactory().updateProductDto(category.get());
        GetResponseProductDto response=  api().getProductSteps().updateProduct(product.get().getId(), requests);
        assertManager().getResponseProductDtoAssert().assertThat(response)
                .verifyTitleIsCorrect(requests.getTitle())
                 .verifyPriceIsCorrect(requests.getPrice())
                .verifyDescriptionIsCorrect(requests.getDescription())
                .verifyCategoryIdIsCorrect(requests.getCategoryId())
                .verifyImagesAreCorrect(requests.getImages());
    }

    @Test
    @RequiresCategory
    @RequiresProduct
    public void testUpdateProductBadRequest() {
        UpdateProductRequestDto requests = factory().productFactory().updateProductWithWrongData();
     api().getProductSteps().updateProductExpectingError(product.get().getId(),requests,HttpStatusCode.BAD_REQUEST,
             PutBadRequestResponse.class);
    }

    @Test
    @RequiresCategory
    @RequiresProduct
    public void testDeleteProductSuccessfully() {
       Response response = api().getProductSteps().deleteProduct(product.get().getId());
        assertManager().getResponseProductDtoAssert().assertThat(response)
               .verifyBooleanResponseIsCorrect();
    }

    @Test
    public void testDeleteProductWrongIdBadRequest() {
     api().getProductSteps().deleteProductExpectingError(factory().getRandomData().getWrongNumber(), HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
    }

}
