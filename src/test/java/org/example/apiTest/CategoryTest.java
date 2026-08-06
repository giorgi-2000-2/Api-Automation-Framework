package org.example.apiTest;
import org.example.apiservice.HttpStatusCode;
import org.example.BaseApiTest;
import org.example.dtos.requestdto.CreateCategoryRequestDto;
import org.example.dtos.requestdto.GetCategoryLimitRequestDto;
import org.example.dtos.requestdto.UpdateCategoryRequestDto;
import org.example.dtos.responsedto.*;
import org.example.annotations.RequiresCategory;
import org.testng.annotations.Test;

import java.util.List;

public class CategoryTest extends BaseApiTest {

    @Test
    public void testCreateCategorySuccessfully() {
        CreateCategoryRequestDto requestBody = factory().categoryFactory().createCategoryWithData();
        GetResponseCategoryDto responseBody = api().getCategorySteps().createCategory(requestBody);
        assertManager().getResponseCategoryDtoAssert().assertThat(responseBody)
                .verifyTitleIsCorrect(requestBody.getName())
                .verifyImageIsCorrect(requestBody.getImage());
        getSoft().assertAll();
    }


    @Test
    public void testCreateCategoryBadRequest(){
        CreateCategoryRequestDto requestBody = factory().categoryFactory().createCategoryWithWrongData();
  api().getCategorySteps().createCategoryExpectingError(requestBody,HttpStatusCode.BAD_REQUEST,
          PutBadRequestResponse.class);
        getSoft().assertAll();
    }


    @Test
    public void testCreateCategoryEmptyFields(){
        CreateCategoryRequestDto requestBody = factory().categoryFactory().createCategoryWithWrongDataEmpty();
       api().getCategorySteps().createCategoryExpectingError(requestBody, HttpStatusCode.BAD_REQUEST,
               ValidationErrorDto.class);
        getSoft().assertAll();
    }

@Test
    public void testGetCategoryLimit(){
    GetCategoryLimitRequestDto requestBody = factory().categoryFactory().getCategoryLimit();
   List<GetResponseCategoryDto>categories = api().getCategorySteps().getCategories(requestBody.getLimit());
   assertManager().getResponseCategoryDtoAssert().assertThat(categories)
            .assertCategoryValidator();
   getSoft().assertAll();
}

    @Test
    @RequiresCategory
    public void testGetCategoryById(){
    GetResponseCategoryDto response = api().getCategorySteps().getCategory(category.get().getId());
        assertManager().getResponseCategoryDtoAssert().assertThat(response)
                    .verifyIdIsCorrect(category.get().getId());
        getSoft().assertAll();
}

@Test
    public void testGetCategoryIdBadRequest(){
  api().getCategorySteps().getCategoryExpectingError(factory().getRandomData().getWrongNumber(),HttpStatusCode.BAD_REQUEST,
          BadRequestResponse.class);
  getSoft().assertAll();
}

    @Test
    @RequiresCategory
    public void testPutCategoryUpdateSuccessfully(){
    UpdateCategoryRequestDto updateCategory = factory().categoryFactory().updateCategoryDto();
    GetResponseCategoryDto response = api().getCategorySteps().updateCategory(category.get().getId(),updateCategory);
        assertManager().getResponseCategoryDtoAssert().assertThat(response)
                    .verifyTitleIsCorrect(updateCategory.getName());
        getSoft().assertAll();
}

    @Test
    @RequiresCategory
    public void testPutCategoryUpdateBadRequest() {
        UpdateCategoryRequestDto updateCategory = factory().categoryFactory().updateCategoryDtoBadRequest();
        api().getCategorySteps().updateCategoryExpectingError(category.get().getId(), updateCategory,HttpStatusCode.BAD_REQUEST,
                PutBadRequestResponse.class);
        getSoft().assertAll();
    }


    @Test
    @RequiresCategory
    public void testDeleteCategorySuccessfully(){
     api().getCategorySteps().deleteCategory(category.get().getId());
     api().getCategorySteps().getCategoryExpectingError(category.get().getId(),HttpStatusCode.BAD_REQUEST,
             BadRequestResponse.class);
        getSoft().assertAll();

    }


    @Test
    public void testDeleteCategoryWrongIdBadRequest(){
   api().getCategorySteps().deleteCategoryExpectingError(factory().getRandomData().getWrongNumber(),
           HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);
        getSoft().assertAll();
    }


    @Test
    @RequiresCategory
    public void testGetCategoryWithSlug(){
        GetResponseCategoryDto response = api().getCategorySteps().getCategoryBySlug(category.get().getSlug());
        assertManager().getResponseCategoryDtoAssert().assertThat(response)
                .verifyTitleIsCorrect(category.get().getName());
        getSoft().assertAll();
}

@Test
    public void testGetCategoryWithWrongSlug(){
 api().getCategorySteps().getCategoryBySlugExpectingError(factory().categoryFactory().emptyField(),HttpStatusCode.BAD_REQUEST,
         BadRequestResponse.class);
    getSoft().assertAll();
}





}
