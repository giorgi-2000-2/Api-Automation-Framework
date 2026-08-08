package ge.gmikeladze.platzi.tests;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.GetCategoryLimitRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.response.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.dtos.response.PutBadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.ValidationErrorDto;
import org.testng.annotations.Test;

import java.util.List;

public class CategoryTest extends BaseApiTest {

    @Test
    public void testCreateCategorySuccessfully() {
        CreateCategoryRequestDto requestBody = categoryData.createCategoryWithData();
        GetResponseCategoryDto responseBody = categorySteps.get().createCategory(requestBody);
        categoryAssert.get().assertThat(responseBody,soft.get())
                .verifyTitleIsCorrect(requestBody.getName())
                .verifyImageIsCorrect(requestBody.getImage());
    }


    @Test
    public void testCreateCategoryBadRequest(){
        CreateCategoryRequestDto requestBody = categoryData.createCategoryWithWrongData();
        categorySteps.get().createCategoryExpectingError(requestBody,HttpStatusCode.BAD_REQUEST,
                PutBadRequestResponse.class);
    }



    @Test
    public void testCreateCategoryEmptyFields(){
        CreateCategoryRequestDto requestBody = categoryData.createCategoryWithWrongDataEmpty();
        categorySteps.get().createCategoryExpectingError(requestBody, HttpStatusCode.BAD_REQUEST,
               ValidationErrorDto.class);
    }

@Test
    public void testGetCategoryLimit(){
    GetCategoryLimitRequestDto requestBody = categoryData.getCategoryLimit();
   List<GetResponseCategoryDto>categories = categorySteps.get().getCategories(requestBody.getLimit());
    categoryAssert.get().assertThat(categories,soft.get())
            .assertCategoryValidator();
}

    @Test
    @RequiresCategory
    public void testGetCategoryById(){
        TestContext ctx = context.get();
    GetResponseCategoryDto response = categorySteps.get().getCategoryById(ctx.getCategory().getId());
        categoryAssert.get().assertThat(response,soft.get())
                    .verifyIdIsCorrect(ctx.getCategory().getId());
}

@Test
    public void testGetCategoryIdBadRequest(){
    categorySteps.get().getCategoryExpectingError(randomData.getWrongNumber(),HttpStatusCode.BAD_REQUEST,
          BadRequestResponse.class);
}

    @Test
    @RequiresCategory
    public void testPutCategoryUpdateSuccessfully(){
        TestContext ctx = context.get();
    UpdateCategoryRequestDto updateCategory = categoryData.updateCategoryDto();
    GetResponseCategoryDto response = categorySteps.get().updateCategory(ctx.getCategory().getId(),updateCategory);
        categoryAssert.get().assertThat(response,soft.get())
                    .verifyTitleIsCorrect(updateCategory.getName());
}

    @Test
    @RequiresCategory
    public void testPutCategoryUpdateBadRequest() {
        TestContext ctx = context.get();
        UpdateCategoryRequestDto updateCategory = categoryData.updateCategoryDtoBadRequest();
        categorySteps.get().updateCategoryExpectingError(ctx.getCategory().getId(), updateCategory,HttpStatusCode.BAD_REQUEST,
                PutBadRequestResponse.class);
    }


    @Test
    @RequiresCategory
    public void testDeleteCategorySuccessfully(){
        TestContext ctx = context.get();
        categorySteps.get().deleteCategory(ctx.getCategory().getId());
        categorySteps.get().getCategoryExpectingError(ctx.getCategory().getId(),HttpStatusCode.BAD_REQUEST,
             BadRequestResponse.class);
    }


    @Test
    public void testDeleteCategoryWrongIdBadRequest(){
        categorySteps.get().deleteCategoryExpectingError(randomData.getWrongNumber(),
           HttpStatusCode.BAD_REQUEST,BadRequestResponse.class);
    }


    @Test
    @RequiresCategory
    public void testGetCategoryWithSlug(){
        TestContext ctx = context.get();
        GetResponseCategoryDto response = categorySteps.get().getCategoryBySlug(ctx.getCategory().getSlug());
        categoryAssert.get().assertThat(response,soft.get())
                .verifyTitleIsCorrect(ctx.getCategory().getName());
}

@Test
    public void testGetCategoryWithWrongSlug(){
    categorySteps.get().getCategoryBySlugExpectingError(categoryData.emptyField(),HttpStatusCode.BAD_REQUEST,
         BadRequestResponse.class);
}




}
