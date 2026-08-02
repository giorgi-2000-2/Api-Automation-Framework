package org.example.ApiTest;
import io.restassured.response.Response;
import org.example.BaseApiTest;
import org.example.DTOs.RequestDto.CreateProductRequestDto;
import org.example.DTOs.RequestDto.UpdateProductRequestDto;
import org.example.DTOs.ResponseDto.GetResponseProductDto;
import org.example.annotations.RequiresCategory;
import org.example.annotations.RequiresProduct;
import org.testng.annotations.Test;

public class ProductTest extends BaseApiTest {

    @Test
    @RequiresCategory
    public void testCreateProductValidData() {
        CreateProductRequestDto requestBody = factory().productFactory().createProductWithData(category.get().getId());
        GetResponseProductDto product =  api().getProductSteps().createProductSuccessfully(requestBody);

        assertManager().getResponseProductDtoAssert().assertThat(product)
                .verifyTitleIsCorrect(requestBody.getTitle())
                .verifyPriceIsCorrect(requestBody.getPrice())
                .verifyDescriptionIsCorrect(requestBody.getDescription())
                .verifyCategoryIdIsCorrect(requestBody.getCategoryId())
                .verifyImagesAreCorrect(requestBody.getImages());

    }

    @Test
    public void testCreateProductInvalidCategoryId() {
        CreateProductRequestDto requestBody = factory().productFactory().createProductWithData(factory().productFactory().getWrongId());
      api().getProductSteps().createProductWithWrongCategoryId(requestBody);

    }


    @Test
    @RequiresCategory
    @RequiresProduct
    public void testGetProductById() {
      GetResponseProductDto responseProductDto= api().getProductSteps().getProductById(product.get().getId());

        assertManager().getResponseProductDtoAssert().assertThat(responseProductDto)
                .verifyTitleIsCorrect(requestBodyProduct.get().getTitle())
                .verifyPriceIsCorrect(requestBodyProduct.get().getPrice())
                .verifyDescriptionIsCorrect(requestBodyProduct.get().getDescription())
                .verifyCategoryIdIsCorrect(requestBodyProduct.get().getCategoryId())
                .verifyImagesAreCorrect(requestBodyProduct.get().getImages());

    }


    @Test
    public void testGetProductByWrongId() {
       api().getProductSteps().getProductByWrongId(factory().productFactory().getWrongId());

    }

    @Test
    @RequiresCategory
    @RequiresProduct
    public void testUpdateProductSuccessfully() {
        UpdateProductRequestDto requests = factory().productFactory().updateProductDto(category.get());
        GetResponseProductDto responseProductDto=  api().getProductSteps().putProduct(product.get().getId(), requests);

        assertManager().getResponseProductDtoAssert().assertThat(responseProductDto)
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
        api().getProductSteps().putProductBadRequest(product.get().getId(), requests);


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
        api().getProductSteps().deleteWithWrongCategoryId(factory().productFactory().getWrongId());




    }

}
