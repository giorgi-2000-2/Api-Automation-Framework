package org.example.ApiTest;
import io.restassured.response.Response;
import org.example.BaseApiTest;
import org.example.DTOs.RequestDto.CreateCategoryRequestDto;
import org.example.DTOs.RequestDto.GetCategoryLimitRequestDTO;
import org.example.DTOs.RequestDto.UpdateCategoryRequestDto;
import org.example.DTOs.ResponseDto.GetResponseCategoryDTO;
import org.example.annotations.RequiresCategory;
import org.testng.annotations.Test;

public class CategoryTest extends BaseApiTest {

    @Test
    public void testCreateCategorySuccessfully() {
        CreateCategoryRequestDto requestBody = factory().categoryFactory().createCategoryWithData();
        GetResponseCategoryDTO responseBody = api().getCategorySteps().createCategorySuccessfully(requestBody,getSoft());

        assertManager().getResponseCategoryDtoAssert().assertThat(responseBody)
                .verifyTitleIsCorrect(requestBody.getName())
                .verifyImageIsCorrect(requestBody.getImage());

        getSoft().assertAll();
    }


    @Test
    public void testCreateCategoryBadRequest(){
        CreateCategoryRequestDto requestBody = factory().categoryFactory().createCategoryWithWrongData();
         api().getCategorySteps().createCategoryBadRequest(requestBody);
    }
    @Test
    public void testCreateCategoryEmptyFields(){
        CreateCategoryRequestDto requestBody = factory().categoryFactory().createCategoryWithWrongDataEmpty();
        api().getCategorySteps().createCategoryBadRequest(requestBody);
    }

@Test
    public void testGetCategoryLimit(){
    GetCategoryLimitRequestDTO requestBody = factory().categoryFactory().getCategoryLimit();
    Response responseCategoryLimit =  api().getCategorySteps().getCategoriesByLimit(requestBody);

    assertManager().getResponseCategoryDtoAssert().assertCategoryValidator(responseCategoryLimit);
    assertManager().getResponseCategoryDtoAssert().assertThat(responseCategoryLimit)
            .assertCategoryValidator(responseCategoryLimit);
}

    @Test
    @RequiresCategory
    public void testGetCategoryById(){
    GetResponseCategoryDTO response = api().getCategorySteps().getCategoryById(category.get().getId(),getSoft());

        assertManager().getResponseCategoryDtoAssert().assertThat(response)
                    .verifyIdIsCorrect(category.get().getId());
        getSoft().assertAll();
}

@Test
    public void testGetCategoryIdBadRequest(){
  api().getCategorySteps().getCategoryByWrongId(factory().categoryFactory().getWrongCategoryId(),getSoft());
  getSoft().assertAll();

}

    @Test
    @RequiresCategory
    public void testPutCategoryUpdateSuccessfully(){
    UpdateCategoryRequestDto updateCategory = factory().categoryFactory().updateCategoryDto();
    GetResponseCategoryDTO response = api().getCategorySteps().putCategoryById(category.get().getId(),updateCategory,getSoft());

        assertManager().getResponseCategoryDtoAssert().assertThat(response)
                    .verifyTitleIsCorrect(updateCategory.getName());
        getSoft().assertAll();
}

    @Test
    @RequiresCategory
    public void testPutCategoryUpdateBadRequest() {
        UpdateCategoryRequestDto updateCategory = factory().categoryFactory().updateCategoryDtoBadRequest();
      api().getCategorySteps().putCategoryByIdBadRequest(category.get().getId(), updateCategory,getSoft());
      getSoft().assertAll();

    }


    @Test
    @RequiresCategory
    public void testDeleteCategorySuccessfully(){
        Response response =  api().getCategorySteps().deleteCategory(category.get().getId());

        assertManager().getResponseCategoryDtoAssert().assertThat(response)
                .verifyBooleanResponseIsCorrect();
    }


    @Test
    public void testDeleteCategoryWrongIdBadRequest(){
       api().getCategorySteps().deleteWithWrongCategoryId(factory().categoryFactory().getWrongCategoryId(),getSoft());
       getSoft().assertAll();

    }


    @Test
    @RequiresCategory
    public void testGetCategoryWithSlug(){
GetResponseCategoryDTO responseCategoryDTO = api().getCategorySteps().getCategoryWithSlug(category.get().getSlug(),getSoft());


        assertManager().getResponseCategoryDtoAssert().assertThat(responseCategoryDTO)
                .verifyTitleIsCorrect(category.get().getName());
        getSoft().assertAll();
}

@Test
    public void testGetCategoryWithWrongSlug(){
 api().getCategorySteps().getCategoryWithWrongSlug(factory().categoryFactory().emptyField(),getSoft());
 getSoft().assertAll();

}





}
