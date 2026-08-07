package ge.gmikeladze.platzi.di;
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
}