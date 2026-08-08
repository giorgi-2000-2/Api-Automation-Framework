package ge.gmikeladze.platzi.datafactories;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.utils.ConfigReader;

import java.util.List;
@Singleton
public class ProductDataFactory {
    private final RandomDataFactory randomDataFactory;
    @Inject
    public ProductDataFactory(RandomDataFactory randomDataFactory) {
        this.randomDataFactory = randomDataFactory;

    }
    public CreateProductRequestDto createProductWithData(int id) {
        return CreateProductRequestDto.builder()
                .title(randomDataFactory.uniqueTitle(ConfigReader.get("categoryName")))
                .price(randomDataFactory.randomInt(1,100))
                .description(randomDataFactory.uniqueTitle("description"))
                .categoryId(id)
                .images(List.of(ConfigReader.get("categoryImage")))
                .build();
    }


    public UpdateProductRequestDto updateProductDto(int id) {
        return UpdateProductRequestDto.builder()
                .title(randomDataFactory.uniqueTitle("updatedName"))
                .price(randomDataFactory.randomInt(1,1000))
                .description("description")
                .categoryId(id)
                .images(List.of(ConfigReader.get("categoryImage")))
                .build();

    }


    public UpdateProductRequestDto updateProductWithWrongData() {
        return UpdateProductRequestDto.builder()
                .title(" ")
                .price(randomDataFactory.randomInt(1,1000))
                .description("description")
                .categoryId(-1)
                .images(List.of(ConfigReader.get("categoryImage")))
                .build();

    }


}
