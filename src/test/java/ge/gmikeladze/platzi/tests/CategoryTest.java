package ge.gmikeladze.platzi.tests;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.negative.CategoryNegativeData;
import ge.gmikeladze.platzi.datafactories.negative.NegativeCase;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.GetCategoryLimitRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.response.error.ApiError;
import ge.gmikeladze.platzi.dtos.response.error.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import org.testng.annotations.Test;

import java.util.List;

public class CategoryTest extends BaseApiTest {


    @Test(groups = {"smoke", "regression","positive"})
    public void testCreateCategorySuccessfully() {

        CreateCategoryRequestDto requestBody = categoryData.createCategoryWithData();
        GetResponseCategoryDto responseBody = categorySteps.get().create(requestBody);
        categoryAssert.get().assertThat(responseBody)
                .hasName(requestBody.getName())
                .hasImage(requestBody.getImage());


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

        GetResponseCategoryDto response = categorySteps.get().getById(context.get().getCategory().getId());
        categoryAssert.get().assertThat(response)
                .hasId(context.get().getCategory().getId());

    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    public void testPutCategoryUpdateSuccessfully() {

        UpdateCategoryRequestDto updateCategory = categoryData.updateCategoryDto();
        GetResponseCategoryDto response = categorySteps.get().update(context.get().getCategory().getId(), updateCategory);
        categoryAssert.get().assertThat(response)
                .hasName(updateCategory.getName());

    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory
    public void testDeleteCategorySuccessfully() {

        categorySteps.get().delete(context.get().getCategory().getId());
        categorySteps.get().getExpectingError(context.get().getCategory().getId(),
                HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);

    }

    @Test(groups = {"smoke", "regression","positive"})
    @RequiresCategory

    public void testGetCategoryWithSlug() {

        GetResponseCategoryDto response = categorySteps.get().getCategoryBySlug(context.get().getCategory().getSlug());
        categoryAssert.get().assertThat(response)
                .hasName(context.get().getCategory().getName());

    }


    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategoryCreate", dataProviderClass = CategoryNegativeData.class)
    public void testCreateCategoryNegative(NegativeCase<CreateCategoryRequestDto> testCase) {
        ApiError error = categorySteps.get().createExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategoryId", dataProviderClass = CategoryNegativeData.class)
    public void testGetCategoryByIdNegative(NegativeCase<Integer> testCase) {
        ApiError error = categorySteps.get().getExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());
    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategoryId", dataProviderClass = CategoryNegativeData.class)
    public void testDeleteCategoryNegative(NegativeCase<Integer> testCase) {
        ApiError error = categorySteps.get().deleteExpectingError(
                testCase.getPayload(), testCase.getExpectedStatus(), testCase.getErrorDto());

        errorAssert.get().assertThat(error)
                .messageIsNotBlank()
                .messageMentionsAll(testCase.getMessageFragments());

    }

    @Test(groups = {"regression","negative"},
            dataProvider = "invalidCategoryUpdate", dataProviderClass = CategoryNegativeData.class)
    @RequiresCategory
    public void testUpdateCategoryNegative(NegativeCase<UpdateCategoryRequestDto> testCase) {

        ApiError error = categorySteps.get().updateExpectingError(
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