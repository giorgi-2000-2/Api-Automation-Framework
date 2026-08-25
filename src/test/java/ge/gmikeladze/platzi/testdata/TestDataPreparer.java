package ge.gmikeladze.platzi;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.RequiresCategory;
import ge.gmikeladze.platzi.annotations.RequiresProduct;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.datafactories.CategoryDataFactory;
import ge.gmikeladze.platzi.datafactories.ProductDataFactory;
import ge.gmikeladze.platzi.di.TestContext;
import ge.gmikeladze.platzi.steps.CategorySteps;
import ge.gmikeladze.platzi.steps.ProductSteps;

import java.lang.reflect.Method;

@TestScoped
public class TestDataPreparer {
    private final CategoryDataFactory categoryData;
    private final ProductDataFactory productData;
    private final CategorySteps categorySteps;
    private final ProductSteps productSteps;
    private final TestContext context;
    @Inject
    public TestDataPreparer(CategoryDataFactory categoryData,
                            ProductDataFactory productData,
                            CategorySteps categorySteps,
                            ProductSteps productSteps,
                            TestContext context) {
        this.categoryData = categoryData;
        this.productData = productData;
        this.categorySteps = categorySteps;
        this.productSteps = productSteps;
        this.context = context;
    }

    public void prepare(Method method) {
        boolean needsCategory = method.isAnnotationPresent(RequiresCategory.class);
        boolean needsProduct  = method.isAnnotationPresent(RequiresProduct.class);

        if (needsCategory || needsProduct) {
            context.setCategoryRequest(categoryData.createCategoryWithData());
            context.setCategory(categorySteps.create(context.getCategoryRequest()));
        }
        if (needsProduct) {
            context.setProductRequest(
                    productData.createProductWithData(context.getCategory().getId()));
            context.setProduct(
                    productSteps.create(context.getProductRequest(), HttpStatusCode.CREATED));
        }
    }
}