package ge.gmikeladze.platzi.datafactories;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.utils.config.IConfigForData;

import java.util.List;

@Singleton
public class ProductDataFactory {
    private final RandomDataFactory randomDataFactory;
    private final IConfigForData config;
    @Inject
    public ProductDataFactory(RandomDataFactory randomDataFactory, IConfigForData config) {
        this.randomDataFactory = randomDataFactory;
        this.config = config;
    }

    public CreateProductRequestDto createProductWithData(int id) {
        return CreateProductRequestDto.builder()
                .title(randomDataFactory.uniqueTitle(config.productName()))
                .price(randomDataFactory.randomInt(1, 100))
                .description(randomDataFactory.uniqueTitle("description"))
                .categoryId(id)
                .images(List.of(config.categoryImage()))
                .build();
    }

    public UpdateProductRequestDto updateProductDto(int id) {
        return UpdateProductRequestDto.builder()
                .title(randomDataFactory.uniqueTitle("productName"))
                .price(randomDataFactory.randomInt(1, 1000))
                .description("description")
                .categoryId(id)
                .images(List.of(config.categoryImage()))
                .build();
    }
}