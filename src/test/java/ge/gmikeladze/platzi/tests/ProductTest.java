package ge.gmikeladze.platzi.tests;
import io.restassured.response.Response;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import ge.gmikeladze.platzi.dtos.response.PutBadRequestResponse;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import org.testng.annotations.Test;

public class ProductTest extends BaseApiTest {

    @Test
    @RequiresCategory
    public void testCreateProductValidData() {
        TestContext ctx = context.get();
        CreateProductRequestDto requestBody = productData.createProductWithData(	ctx.getCategory().getId());
        GetResponseProductDto response =  	productSteps.get().createProduct(requestBody);
        productAssert.get().assertThat(response,soft.get())
                .verifyTitleIsCorrect(requestBody.getTitle())
                .verifyPriceIsCorrect(requestBody.getPrice())
                .verifyDescriptionIsCorrect(requestBody.getDescription())
                .verifyCategoryIdIsCorrect(requestBody.getCategoryId())
                .verifyImagesAreCorrect(requestBody.getImages());
    }

    @Test
    public void testCreateProductInvalidCategoryId() {
        CreateProductRequestDto requestBody = productData.createProductWithData(randomData.getWrongNumber());
        productSteps.get().createProductExpectingError(requestBody,HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);
    }


    @Test
    @RequiresCategory
    @RequiresProduct
    public void testGetProductById() {
        TestContext ctx = context.get();
      GetResponseProductDto response= 	productSteps.get().getProduct(ctx.getProduct().getId());
        productAssert.get().assertThat(response,soft.get())
                .verifyTitleIsCorrect(	ctx.getProductRequest().getTitle())
                .verifyPriceIsCorrect(	ctx.getProductRequest().getPrice())
                .verifyDescriptionIsCorrect(	ctx.getProductRequest().getDescription())
                .verifyCategoryIdIsCorrect(	ctx.getProductRequest().getCategoryId())
                .verifyImagesAreCorrect(	ctx.getProductRequest().getImages());
    }


    @Test
    public void testGetProductByWrongId() {
        productSteps.get().getProductExpectingError(randomData.getWrongNumber(),HttpStatusCode.BAD_REQUEST,
               BadRequestResponse.class);
    }

    @Test
    @RequiresCategory
    @RequiresProduct
    public void testUpdateProductSuccessfully() {
        TestContext ctx = context.get();
        UpdateProductRequestDto requests = productData.updateProductDto(ctx.getCategory().getId());
        GetResponseProductDto response = productSteps.get().updateProduct(ctx.getProduct().getId(), requests);
        productAssert.get().assertThat(response,soft.get())
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
        TestContext ctx = context.get();
        UpdateProductRequestDto requests = productData.updateProductWithWrongData();
        productSteps.get().updateProductExpectingError(ctx.getProduct().getId(),requests,HttpStatusCode.BAD_REQUEST,
             PutBadRequestResponse.class);
        //put ერორს და create bad request ს ერთნაირი dto აქვს.
    }

    @Test
    @RequiresCategory
    @RequiresProduct
    public void testDeleteProductSuccessfully() {
        TestContext ctx = context.get();
       Response response = 	productSteps.get().deleteProduct(ctx.getProduct().getId());
        productAssert.get().assertThat(response,soft.get())
               .verifyBooleanResponseIsCorrect();
    }

    @Test
    public void testDeleteProductWrongIdBadRequest() {
        productSteps.get().deleteProductExpectingError(randomData.getWrongNumber(), HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
    }


}
