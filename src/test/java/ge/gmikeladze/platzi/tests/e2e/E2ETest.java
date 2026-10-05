package ge.gmikeladze.platzi.tests.e2e;
import ge.gmikeladze.platzi.BaseApiTest;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import ge.gmikeladze.platzi.dtos.request.*;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import org.testng.annotations.Test;
import java.util.List;


public class E2ETest extends BaseApiTest {

@Test(groups = {"e2e", "regression"})
@RequiresCategory
@RequiresProduct
public void productAndCategoryLifecycle(){
    CreateProductRequestDto createdWith = context.get().getProductRequest();

    GetResponseProductDto original = e2eSteps.get().fetchProduct( context.get().getProduct().getId());
    productAssert.get().assertThat(original)
            .hasCategoryId(createdWith.getCategoryId())
            .hasTitle(createdWith.getTitle());

    UpdateCategoryRequestDto categoryUpdate = categoryData.updateCategoryDto();
    GetResponseCategoryDto category = e2eSteps.get().updateCategory(categoryUpdate,context.get().getCategory().getId());
    categoryAssert.get().assertThat(category)
            .hasId(context.get().getCategory().getId())
            .hasName(categoryUpdate.getName());

    UpdateProductRequestDto productUpdate = e2eSteps.get().newProductUpdate(context.get().getCategory().getId());
    GetResponseProductDto product = e2eSteps.get().updateProduct(original.getId(), productUpdate);
    productAssert.get().assertThat(product)
            .hasPrice(productUpdate.getPrice())
            .hasCategoryId(category.getId());

    productAssert.get().assertThat(e2eSteps.get().deleteProductAndVerifyGone(product.getId()))
            .isDeletedSuccessfully();
    categoryAssert.get().assertThat(e2eSteps.get().deleteCategoryAndVerifyGone(context.get().getCategory().getId()))
            .isDeletedSuccessfully();

    }




    @Test(groups = {"e2e", "regression"})
    @RequiresCategory
    @RequiresProduct
    public void testProductUpdateAndCleanupCompletesSuccessfully() {
        GetResponseProductDto second = e2eSteps.get().addProductToCategory(context.get().getCategory().getId());
        productAssert.get().assertThat(e2eSteps.get().productsInCategory(context.get().getCategory().getId())).hasSize(2);

        UpdateProductRequestDto update = e2eSteps.get().newProductUpdate(context.get().getCategory().getId());
        GetResponseProductDto updated = e2eSteps.get().updateProduct(second.getId(), update);
        productAssert.get().assertThat(updated).hasTitle(update.getTitle());
        productAssert.get().assertThat(e2eSteps.get().productsInCategory(context.get().getCategory().getId())).hasSize(2);

        e2eSteps.get().deleteProductAndVerifyGone(updated.getId());
        e2eSteps.get().deleteProductAndVerifyGone(context.get().getProduct().getId());

    }

    @Test(groups = {"e2e", "regression"})
    @RequiresCategory
    @RequiresProduct
    public void testCategoryFlowWithProductRemoval() {
        e2eSteps.get().addProductToCategory(context.get().getCategory().getId());
        GetResponseProductDto third = e2eSteps.get().addProductToCategory(context.get().getCategory().getId());
        productAssert.get().assertThat(e2eSteps.get().productsInCategory(context.get().getCategory().getId())).hasSize(3);
        e2eSteps.get().deleteProductAndVerifyGone(third.getId());
        productAssert.get().assertThat(e2eSteps.get().productsInCategory(context.get().getCategory().getId())).hasSize(2);

    }

    @Test(groups = {"regression", "positive", "auth", "e2e"})
    public void testRegisterThenLoginThenProfile() {

        CreateUserDto newUser = userData.createUserWithData();
        GetUserResponseDto created = userSteps.get().create(newUser);

        LoginRequestDto loginRequestDto = authData.loginWith(created.getEmail(), newUser.getPassword());
        authSteps.get().login(loginRequestDto);
        GetUserResponseDto profile = authSteps.get().getProfile();
        userAssert.get().assertThat(profile)
                .hasEmail(created.getEmail())
                .hasName(created.getName());
    }

    @Test(groups = {"regression", "positive", "auth", "e2e"})
    public void testRefreshedTokenReturnsSameProfile() {
        CreateUserDto newUser = userData.createUserWithData();
        GetUserResponseDto beforeRefresh = e2eSteps.get().registerLoginAndGetProfile(newUser);

        GetUserResponseDto afterRefresh = e2eSteps.get().refreshAndGetProfile();

        userAssert.get().assertThat(afterRefresh)
                .hasId(beforeRefresh.getId())
                .hasEmail(newUser.getEmail())
                .hasName(newUser.getName());
    }



    @Test(groups = {"regression", "positive", "e2e"})
    @RequiresCategory
    public void testProductsByCategoryPagination() {
        e2eSteps.get().addProductToCategory(context.get().getCategory().getId());
        e2eSteps.get().addProductToCategory(context.get().getCategory().getId());
        e2eSteps.get().addProductToCategory(context.get().getCategory().getId());

        List<GetResponseProductDto> firstPage = e2eSteps.get().productsInCategory(context.get().getCategory().getId(),2, 0);
        List<GetResponseProductDto> secondPage = e2eSteps.get().productsInCategory(context.get().getCategory().getId(),2, 2);

        productAssert.get().assertThat(firstPage).hasSize(2);
        productAssert.get().assertThat(secondPage).hasSize(1);
        soft.get().assertNotEquals(
                firstPage.getFirst().getId(),
                secondPage.getFirst().getId(),
                " განსხვავებული პროდუქტები");
    }

    @Test(groups = {"e2e", "regression"})
    @RequiresCategory
    public void testCleanupRemovesProductsThenCategoryInLifoOrder() {
        int firstId = e2eSteps.get().addProductToCategory(context.get().getCategory().getId()).getId();
        int secondId = e2eSteps.get().addProductToCategory(context.get().getCategory().getId()).getId();

        context.get().getCleanupRegistry().cleanup();

        e2eSteps.get().verifyProductGone(firstId);
        e2eSteps.get().verifyProductGone(secondId);
        e2eSteps.get().verifyCategoryGone(context.get().getCategory().getId());
    }

    @Test(groups = {"e2e", "regression"})
    @RequiresCategory
    public void testProductListReflectsDeleteWithinCategory() {
        GetResponseProductDto first = e2eSteps.get().addProductToCategory(context.get().getCategory().getId());
        GetResponseProductDto second = e2eSteps.get().addProductToCategory(context.get().getCategory().getId());
        productAssert.get().assertThat(e2eSteps.get().productsInCategory(context.get().getCategory().getId())).hasSize(2);

        e2eSteps.get().deleteProductAndVerifyGone(first.getId());

        List<GetResponseProductDto> remaining = e2eSteps.get().productsInCategory(context.get().getCategory().getId());
        productAssert.get().assertThat(remaining).hasSize(1);
        soft.get().assertEquals(remaining.getFirst().getId(), second.getId(),
                "დარჩენილი product-ის id უნდა ემთხვეოდეს არაწაშლილს");
        soft.get().assertEquals(remaining.getFirst().getTitle(), second.getTitle(),
                "დარჩენილი product-ის title უნდა ემთხვეოდეს არაწაშლილს");
    }



}


