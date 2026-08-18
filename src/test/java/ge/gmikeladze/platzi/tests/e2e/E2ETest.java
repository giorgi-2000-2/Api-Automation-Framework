package ge.gmikeladze.platzi.tests.e2e;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.error.BadRequestResponse;
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
    GetResponseProductDto response = productSteps.get().getById(context.get().getProduct().getId());
    productAssert.get().assertThat(response)
            .verifyCategoryIdIsCorrect(context.get().getProductRequest().getCategoryId())
            .verifyTitleIsCorrect(context.get().getProductRequest().getTitle());

    UpdateCategoryRequestDto updateCategoryRequestDto = categoryData.updateCategoryDto();
    GetResponseCategoryDto responseCategoryDto = categorySteps.get().update(context.get().getCategory().getId(),updateCategoryRequestDto);

    UpdateProductRequestDto requestDto = productData.updateProductDto(responseCategoryDto.getId());
    GetResponseProductDto responseProductDto = productSteps.get().update(context.get().getProduct().getId(),requestDto);

    categoryAssert.get().assertThat(responseCategoryDto)
            .verifyIdIsCorrect(context.get().getCategory().getId())
            .verifyTitleIsCorrect(updateCategoryRequestDto.getName());

    productAssert.get().assertThat(responseProductDto)
            .verifyPriceIsCorrect(requestDto.getPrice())
            .verifyCategoryIdIsCorrect(responseCategoryDto.getId());

    Response responseDeleteProduct = productSteps.get().delete(responseProductDto.getId());
    productAssert.get().assertThat(responseDeleteProduct)
            .verifyBooleanResponseIsCorrect();

    Response responseDeleteCategory = categorySteps.get().delete(responseCategoryDto.getId());
    categoryAssert.get().assertThat(responseDeleteCategory)
            .verifyBooleanResponseIsCorrect();

   productSteps.get().getExpectingError(responseProductDto.getId(), HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
   categorySteps.get().getExpectingError(responseCategoryDto.getId(), HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);

    }




    @Test(groups = {"e2e", "regression"})
    @RequiresCategory
    @RequiresProduct
    public void testProductUpdateAndCleanupCompletesSuccessfully() {

        CreateProductRequestDto secondProductRequestDto = productData.createProductWithData(context.get().getCategory().getId());

        GetResponseProductDto secondProductDto = productSteps.get().create(secondProductRequestDto);

        List<GetResponseProductDto> responseProductDtos = productSteps.get().getProductsByCategoryId(context.get().getCategory().getId(),HttpStatusCode.OK);

        soft.get().assertEquals(responseProductDtos.size(),2);

        UpdateProductRequestDto responseUpdatedProductDto = productData.updateProductDto(context.get().getCategory().getId());

        GetResponseProductDto getResponseUpdatedProduct = productSteps.get().update(secondProductDto.getId(),responseUpdatedProductDto);

        productAssert.get().assertThat(getResponseUpdatedProduct)
                        .verifyTitleIsCorrect(responseUpdatedProductDto.getTitle());

        List<GetResponseProductDto> responseProductAfterUpdate = productSteps.get().getProductsByCategoryId(context.get().getCategory().getId(),HttpStatusCode.OK);

        soft.get().assertEquals(responseProductAfterUpdate.size(),2);

      productSteps.get().delete(getResponseUpdatedProduct.getId());
       productSteps.get().getExpectingError(getResponseUpdatedProduct.getId(),HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);

       productSteps.get().delete(context.get().getProduct().getId());
        productSteps.get().getExpectingError(context.get().getProduct().getId(),HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);

    }

    @Test(groups = {"e2e", "regression"})
    @RequiresCategory
    @RequiresProduct
    public void testCategoryFlowWithProductRemoval() {
       GetResponseProductDto secondProduct =  productSteps.get().create(productData.createProductWithData(context.get().getCategory().getId()));
       GetResponseProductDto thirdProduct =  productSteps.get().create(productData.createProductWithData(context.get().getCategory().getId()));

        List<GetResponseProductDto> paginatedProducts = categorySteps.get()
                .getProductsByCategoryIdWithPagination(context.get().getCategory().getId(), 0, 0, HttpStatusCode.OK);

        soft.get().assertEquals(paginatedProducts.size(), 3);

productSteps.get().delete(thirdProduct.getId());

        List<GetResponseProductDto> paginatedProductsAfterDelete = categorySteps.get()
                .getProductsByCategoryIdWithPagination(context.get().getCategory().getId(), 0, 0, HttpStatusCode.OK);

soft.get().assertEquals(paginatedProductsAfterDelete.size(),2);

        soft.get().assertNotEquals(thirdProduct.getTitle(), secondProduct.getTitle());



    }




}
