package ge.gmikeladze.platzi.tests;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.CategoryNegativeData;
import ge.gmikeladze.platzi.datafactories.NegativeCase;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.GetCategoryLimitRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.response.ApiError;
import ge.gmikeladze.platzi.dtos.response.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import org.testng.annotations.Test;

import java.util.List;

public class CategoryTest extends BaseApiTest {


    @Test(groups = {"smoke", "regression","positive"})
    public void testCreateCategorySuccessfully() {

        CreateCategoryRequestDto requestBody = categoryData.createCategoryWithData();
        GetResponseCategoryDto responseBody = categorySteps.get().createCategory(requestBody);
        categoryAssert.get().assertThat(responseBody)
                .verifyTitleIsCorrect(requestBody.getName())
                .verifyImageIsCorrect(requestBody.getImage());


    }

    @Test(groups = { "regression","positive"})
    public void testGetCategoryLimit() {
        GetCategoryLimitRequestDto requestBody = categoryData.getCategoryLimit();
        List<GetResponseCategoryDto> categories = categorySteps.get().getCategories(requestBody.getLimit());
        categoryAssert.get().assertThat(categories)
                .assertCategoryValidator(requestBody.getLimit());
    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    public void testGetCategoryById() {

        GetResponseCategoryDto response = categorySteps.get().getCategoryById(context.get().getCategory().getId());
        categoryAssert.get().assertThat(response)
                .verifyIdIsCorrect(context.get().getCategory().getId());

    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    public void testPutCategoryUpdateSuccessfully() {

        UpdateCategoryRequestDto updateCategory = categoryData.updateCategoryDto();
        GetResponseCategoryDto response = categorySteps.get().updateCategory(context.get().getCategory().getId(), updateCategory);
        categoryAssert.get().assertThat(response)
                .verifyTitleIsCorrect(updateCategory.getName());

    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    public void testDeleteCategorySuccessfully() {

        categorySteps.get().deleteCategory(context.get().getCategory().getId());
        categorySteps.get().getCategoryExpectingError(context.get().getCategory().getId(),
                HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);

    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory

    public void testGetCategoryWithSlug() {

        GetResponseCategoryDto response = categorySteps.get().getCategoryBySlug(context.get().getCategory().getSlug());
        categoryAssert.get().assertThat(response)
                .verifyTitleIsCorrect(context.get().getCategory().getName());

    }


    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategoryCreate", dataProviderClass = CategoryNegativeData.class)
    public void testCreateCategoryNegative(NegativeCase<CreateCategoryRequestDto> testCase) {
        ApiError error = categorySteps.get().createCategoryExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategoryId", dataProviderClass = CategoryNegativeData.class)
    public void testGetCategoryByIdNegative(NegativeCase<Integer> testCase) {
        ApiError error = categorySteps.get().getCategoryExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategoryId", dataProviderClass = CategoryNegativeData.class)
    public void testDeleteCategoryNegative(NegativeCase<Integer> testCase) {
        ApiError error = categorySteps.get().deleteCategoryExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());

    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategoryUpdate", dataProviderClass = CategoryNegativeData.class)
    @RequiresCategory
    public void testUpdateCategoryNegative(NegativeCase<UpdateCategoryRequestDto> testCase) {

        ApiError error = categorySteps.get().updateCategoryExpectingError(
                context.get().getCategory().getId(), testCase.getPayload(),
                testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());

    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategorySlug", dataProviderClass = CategoryNegativeData.class)
    public void testGetCategoryBySlugNegative(NegativeCase<String> testCase) {
        ApiError error = categorySteps.get().getCategoryBySlugExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }
}