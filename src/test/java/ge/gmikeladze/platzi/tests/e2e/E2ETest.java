package ge.gmikeladze.platzi.tests.e2e;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import java.util.List;


public class E2ETest extends BaseApiTest {

@Test
@RequiresCategory
@RequiresProduct
    public void ProductAndCategoryLifecycle(){
    TestContext ctx = context.get();
    GetResponseProductDto response = productSteps.get().getProduct(ctx.getProduct().getId());
    productAssert.get().assertThat(response,soft.get())
            .verifyCategoryIdIsCorrect(ctx.getProductRequest().getCategoryId())
            .verifyTitleIsCorrect(ctx.getProductRequest().getTitle());


    UpdateCategoryRequestDto updateCategoryRequestDto = categoryData.updateCategoryDto();
    GetResponseCategoryDto responseCategoryDto = categorySteps.get().updateCategory(ctx.getCategory().getId(),updateCategoryRequestDto);

    UpdateProductRequestDto requestDto = productData.updateProductDto(responseCategoryDto.getId());
    GetResponseProductDto responseProductDto = productSteps.get().updateProduct(ctx.getProduct().getId(),requestDto);

    categoryAssert.get().assertThat(responseCategoryDto,soft.get())
            .verifyIdIsCorrect(ctx.getCategory().getId())
            .verifyTitleIsCorrect(updateCategoryRequestDto.getName());

    productAssert.get().assertThat(responseProductDto,soft.get())
            .verifyPriceIsCorrect(requestDto.getPrice())
            .verifyCategoryIdIsCorrect(responseCategoryDto.getId());

    Response responseDeleteProduct = productSteps.get().deleteProduct(responseProductDto.getId());
    productAssert.get().assertThat(responseDeleteProduct,soft.get())
            .verifyBooleanResponseIsCorrect();

    Response responseDeleteCategory = categorySteps.get().deleteCategoryById(responseCategoryDto.getId());
    categoryAssert.get().assertThat(responseDeleteCategory,soft.get())
            .verifyBooleanResponseIsCorrect();

   productSteps.get().getProductExpectingError(responseProductDto.getId(), HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
   categorySteps.get().getCategoryExpectingError(responseCategoryDto.getId(), HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);

    }




    @Test
    @RequiresCategory
    @RequiresProduct
    public void ProductFilteringDynamicPriceUpdateFlow() {
        TestContext ctx = context.get();

        CreateProductRequestDto secondProductRequestDto = productData.createProductWithData(ctx.getCategory().getId());

        GetResponseProductDto secondProductDto = productSteps.get().createProduct(secondProductRequestDto);

        List<GetResponseProductDto> responseProductDtos = productSteps.get().getProductsByCategoryId(ctx.getCategory().getId(),HttpStatusCode.OK);

        soft.get().assertEquals(responseProductDtos.size(),2);

        UpdateProductRequestDto responseUpdatedProductDto = productData.updateProductDto(secondProductDto.getId());

        GetResponseProductDto getResponseUpdatedProduct = productSteps.get().updateProduct(secondProductDto.getId(),responseUpdatedProductDto);

        productAssert.get().assertThat(getResponseUpdatedProduct,soft.get())
                        .verifyTitleIsCorrect(secondProductDto.getTitle());

        List<GetResponseProductDto> responseProductAfterUpdate = productSteps.get().getProductsByCategoryId(ctx.getCategory().getId(),HttpStatusCode.OK);

        soft.get().assertEquals(responseProductAfterUpdate.size(),2);

      productSteps.get().deleteProduct(getResponseUpdatedProduct.getId());
       productSteps.get().getProductExpectingError(getResponseUpdatedProduct.getId(),HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);

       productSteps.get().deleteProduct(ctx.getProduct().getId());
        productSteps.get().getProductExpectingError(ctx.getProduct().getId(),HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);

    }

    @Test
    @RequiresCategory
    @RequiresProduct
    public void categoryProductsPaginationTest() {
        TestContext ctx = context.get();

       GetResponseProductDto secondProduct =  productSteps.get().createProduct(productData.createProductWithData(ctx.getCategory().getId()));
       GetResponseProductDto thirdProduct =  productSteps.get().createProduct(productData.createProductWithData(ctx.getCategory().getId()));

        List<GetResponseProductDto> paginatedProducts = categorySteps.get()
                .getProductsByCategoryIdWithPagination(ctx.getCategory().getId(), 0, 0, HttpStatusCode.OK);

        soft.get().assertEquals(paginatedProducts.size(), 3);

productSteps.get().deleteProduct(thirdProduct.getId());

        List<GetResponseProductDto> paginatedProductsAfterDelete = categorySteps.get()
                .getProductsByCategoryIdWithPagination(ctx.getCategory().getId(), 0, 0, HttpStatusCode.OK);

soft.get().assertEquals(paginatedProductsAfterDelete.size(),2);

productAssert.get().assertThat(thirdProduct,soft.get())
                .verifyTitleIsCorrect(secondProduct.getTitle())
                .verifyTitleIsCorrect(ctx.getProduct().getTitle());



    }




}
