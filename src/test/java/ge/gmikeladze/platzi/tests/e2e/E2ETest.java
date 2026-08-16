package ge.gmikeladze.platzi.tests.e2e;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
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

@Test(groups = {"e2e", "regression"})
@RequiresCategory
@RequiresProduct
public void ProductAndCategoryLifecycle(){
    GetResponseProductDto response = productSteps.get().getProduct(context.get().getProduct().getId());
    productAssert.get().assertThat(response)
            .verifyCategoryIdIsCorrect(context.get().getProductRequest().getCategoryId())
            .verifyTitleIsCorrect(context.get().getProductRequest().getTitle());

    UpdateCategoryRequestDto updateCategoryRequestDto = categoryData.updateCategoryDto();
    GetResponseCategoryDto responseCategoryDto = categorySteps.get().updateCategory(context.get().getCategory().getId(),updateCategoryRequestDto);

    UpdateProductRequestDto requestDto = productData.updateProductDto(responseCategoryDto.getId());
    GetResponseProductDto responseProductDto = productSteps.get().updateProduct(context.get().getProduct().getId(),requestDto);

    categoryAssert.get().assertThat(responseCategoryDto)
            .verifyIdIsCorrect(context.get().getCategory().getId())
            .verifyTitleIsCorrect(updateCategoryRequestDto.getName());

    productAssert.get().assertThat(responseProductDto)
            .verifyPriceIsCorrect(requestDto.getPrice())
            .verifyCategoryIdIsCorrect(responseCategoryDto.getId());

    Response responseDeleteProduct = productSteps.get().deleteProduct(responseProductDto.getId());
    productAssert.get().assertThat(responseDeleteProduct)
            .verifyBooleanResponseIsCorrect();

    Response responseDeleteCategory = categorySteps.get().deleteCategory(responseCategoryDto.getId());
    categoryAssert.get().assertThat(responseDeleteCategory)
            .verifyBooleanResponseIsCorrect();

   productSteps.get().getProductExpectingError(responseProductDto.getId(), HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
   categorySteps.get().getCategoryExpectingError(responseCategoryDto.getId(), HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);

    }




    @Test(groups = {"e2e", "regression"})
    @RequiresCategory
    @RequiresProduct
    public void testProductUpdateAndCleanupCompletesSuccessfully() {

        CreateProductRequestDto secondProductRequestDto = productData.createProductWithData(context.get().getCategory().getId());

        GetResponseProductDto secondProductDto = productSteps.get().createProduct(secondProductRequestDto);

        List<GetResponseProductDto> responseProductDtos = productSteps.get().getProductsByCategoryId(context.get().getCategory().getId(),HttpStatusCode.OK);

        soft.get().assertEquals(responseProductDtos.size(),2);

        UpdateProductRequestDto responseUpdatedProductDto = productData.updateProductDto(context.get().getCategory().getId());

        GetResponseProductDto getResponseUpdatedProduct = productSteps.get().updateProduct(secondProductDto.getId(),responseUpdatedProductDto);

        productAssert.get().assertThat(getResponseUpdatedProduct)
                        .verifyTitleIsCorrect(responseUpdatedProductDto.getTitle());

        List<GetResponseProductDto> responseProductAfterUpdate = productSteps.get().getProductsByCategoryId(context.get().getCategory().getId(),HttpStatusCode.OK);

        soft.get().assertEquals(responseProductAfterUpdate.size(),2);

      productSteps.get().deleteProduct(getResponseUpdatedProduct.getId());
       productSteps.get().getProductExpectingError(getResponseUpdatedProduct.getId(),HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);

       productSteps.get().deleteProduct(context.get().getProduct().getId());
        productSteps.get().getProductExpectingError(context.get().getProduct().getId(),HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);

    }

    @Test(groups = {"e2e", "regression"})
    @RequiresCategory
    @RequiresProduct
    public void testCategoryFlowWithProductRemoval() {
       GetResponseProductDto secondProduct =  productSteps.get().createProduct(productData.createProductWithData(context.get().getCategory().getId()));
       GetResponseProductDto thirdProduct =  productSteps.get().createProduct(productData.createProductWithData(context.get().getCategory().getId()));

        List<GetResponseProductDto> paginatedProducts = categorySteps.get()
                .getProductsByCategoryIdWithPagination(context.get().getCategory().getId(), 0, 0, HttpStatusCode.OK);

        soft.get().assertEquals(paginatedProducts.size(), 3);

productSteps.get().deleteProduct(thirdProduct.getId());

        List<GetResponseProductDto> paginatedProductsAfterDelete = categorySteps.get()
                .getProductsByCategoryIdWithPagination(context.get().getCategory().getId(), 0, 0, HttpStatusCode.OK);

soft.get().assertEquals(paginatedProductsAfterDelete.size(),2);

        soft.get().assertNotEquals(thirdProduct.getTitle(), secondProduct.getTitle());



    }




}
