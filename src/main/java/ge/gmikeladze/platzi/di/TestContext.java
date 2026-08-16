package ge.gmikeladze.platzi.di;
import com.google.inject.Inject;
import ge.gmikeladze.platzi.annotations.TestScoped;
import ge.gmikeladze.platzi.cleanup.CleanupRegistry;
import lombok.Getter;
import lombok.Setter;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;


@Getter
@Setter
@TestScoped
public class TestContext {
    private CreateCategoryRequestDto categoryRequest;
    private GetResponseCategoryDto   category;
    private CreateProductRequestDto  productRequest;
    private GetResponseProductDto    product;
    private CleanupRegistry cleanupRegistry;

    @Inject
    public TestContext(CleanupRegistry cleanupRegistry) {
        this.cleanupRegistry = cleanupRegistry;
    }

}