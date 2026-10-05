package ge.gmikeladze.platzi.steps;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiclient.Pagination;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.assertions.ResponseValidator;
import ge.gmikeladze.platzi.datafactories.AuthDataFactory;
import ge.gmikeladze.platzi.datafactories.ProductDataFactory;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.dtos.request.CreateUserDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import ge.gmikeladze.platzi.dtos.response.GetUserResponseDto;
import ge.gmikeladze.platzi.dtos.response.error.BadRequestResponse;
import ge.gmikeladze.platzi.utils.reporter.ITestReporter;
import io.restassured.response.Response;

import java.util.List;

@TestScoped
public class E2ESteps extends BaseSteps {
    private final CategorySteps categorySteps;
    private final ProductSteps productSteps;
    private final UserSteps userSteps;
    private final AuthSteps authSteps;
    private final ProductDataFactory productData;
    private final AuthDataFactory authData;
    private final TestContext context;

    @Inject
    public E2ESteps( CategorySteps categorySteps,
                    ProductSteps productSteps,
                    UserSteps userSteps,
                    AuthSteps authSteps,
                    ProductDataFactory productData,
                    AuthDataFactory authData,
                    TestContext context,
                    ITestReporter reporter, ResponseValidator validator ) {
        super(reporter,validator);
        this.categorySteps = categorySteps;
        this.productSteps = productSteps;
        this.userSteps = userSteps;
        this.authSteps = authSteps;
        this.productData = productData;
        this.authData = authData;
        this.context = context;
    }


    /** ქმნის ახალ პროდუქტს ტესტის კატეგორიაში. */
    public GetResponseProductDto addProductToCategory(int id) {
        step("პროდუქტის დამატება ტესტის კატეგორიაში");
        return productSteps.create(productData.createProductWithData(id));
    }

    /** აბრუნებს ტესტის კატეგორიის ყველა პროდუქტს. */
    public List<GetResponseProductDto> productsInCategory(int id) {
        step("ტესტის კატეგორიის პროდუქტების წამოღება");
        return categorySteps.getProductsByCategoryId(id, Pagination.none(), HttpStatusCode.OK);
    }

    /** თავიდან კითხულობს @RequiresProduct-ით შექმნილ პროდუქტს. */
    public GetResponseProductDto fetchProduct(int id) {
        step("ტესტის პროდუქტის წამოღება");
        return productSteps.getById(id);
    }

    /** ამზადებს პროდუქტის განახლების მონაცემებს ტესტის კატეგორიისთვის. */
    public UpdateProductRequestDto newProductUpdate(int id) {
        return productData.updateProductDto(id);
    }

    /** ანახლებს პროდუქტს და აბრუნებს GET-ით თავიდან წაკითხულ ჩანაწერს. */
    public GetResponseProductDto updateProduct(int productId, UpdateProductRequestDto update) {
        step("პროდუქტის განახლება და თავიდან წაკითხვა, id=" + productId);
        productSteps.update(productId, update);
        return productSteps.getById(productId);
    }

    /** ანახლებს ტესტის კატეგორიას და აბრუნებს GET-ით თავიდან წაკითხულ ჩანაწერს. */
    public GetResponseCategoryDto updateCategory(UpdateCategoryRequestDto update,int id) {
        step("ტესტის კატეგორიის განახლება და თავიდან წაკითხვა");
        categorySteps.update(id, update);
        return categorySteps.getById(id);
    }

    /** შლის პროდუქტს და ამოწმებს, რომ GET მას ვეღარ პოულობს. აბრუნებს წაშლის პასუხს. */
    public Response deleteProductAndVerifyGone(int productId) {
        step("პროდუქტის წაშლა და შემოწმება, რომ აღარ არსებობს, id=" + productId);
        Response deleted = productSteps.delete(productId);
        productSteps.getExpectingError(productId, HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
        return deleted;
    }

    /** შლის ტესტის კატეგორიას და ამოწმებს, რომ GET მას ვეღარ პოულობს. აბრუნებს წაშლის პასუხს. */
    public Response deleteCategoryAndVerifyGone(int id) {
        step("ტესტის კატეგორიის წაშლა და შემოწმება, რომ აღარ არსებობს");
        Response deleted = categorySteps.delete(id);
        categorySteps.getExpectingError(id, HttpStatusCode.BAD_REQUEST, BadRequestResponse.class);
        return deleted;
    }
    public List<GetResponseProductDto> productsInCategory(int id,int limit,int offset) {
        step("ტესტის კატეგორიის პროდუქტების წამოღება");
        return categorySteps.getProductsByCategoryId(id, Pagination.of(limit, offset), HttpStatusCode.OK);
    }

    /** ამოწმებს, რომ პროდუქტი აღარ არსებობს: GET მას ვეღარ პოულობს. */
    public void verifyProductGone(int productId) {
        step("შემოწმება, რომ პროდუქტი აღარ არსებობს, id=" + productId);
        productSteps.verifyGone(productId);
    }

    /** ამოწმებს, რომ ტესტის კატეგორია აღარ არსებობს: GET მას ვეღარ პოულობს. */
    public void verifyCategoryGone(int id) {
        step("შემოწმება, რომ ტესტის კატეგორია აღარ არსებობს");
        categorySteps.verifyGone(id);
    }

    /** არეგისტრირებს მომხმარებელს, შედის მისი მონაცემებით და აბრუნებს პროფილს. */
    public GetUserResponseDto registerLoginAndGetProfile(CreateUserDto newUser) {
        step("რეგისტრაცია, შესვლა და პროფილის წამოღება: " + newUser.getEmail());
        userSteps.create(newUser);
        authSteps.login(authData.loginWith(newUser.getEmail(), newUser.getPassword()));
        return authSteps.getProfile();
    }

    /** ანახლებს token-ს და აბრუნებს პროფილს ახალი token-ით. მანამდე საჭიროა შესვლა. */
    public GetUserResponseDto refreshAndGetProfile() {
        step("token-ის განახლება და პროფილის წამოღება ახალი token-ით");
        String refreshToken = context.getRefreshToken();
        if (refreshToken == null) {
            throw new IllegalStateException(
                    "refresh token არ არის: ჯერ გამოიძახე registerLoginAndGetProfile(...)");
        }
        authSteps.refresh(refreshToken);
        return authSteps.getProfile();
    }




}