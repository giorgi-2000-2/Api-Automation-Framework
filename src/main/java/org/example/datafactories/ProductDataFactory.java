package org.example.datafactories;
import org.example.dtos.requestdto.CreateProductRequestDto;
import org.example.dtos.requestdto.UpdateProductRequestDto;
import org.example.dtos.responsedto.GetResponseCategoryDto;
import org.example.utils.ConfigReader;

import java.util.List;

public class ProductDataFactory {
    private RandomDataFactory randomDataFactory;

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


    public UpdateProductRequestDto updateProductDto(GetResponseCategoryDto response) {
        return UpdateProductRequestDto.builder()
                .title(randomDataFactory.uniqueTitle("updatedName"))
                .price(randomDataFactory.randomInt(0,1000))
                .description("description")
                .categoryId(response.getId())
                .images(List.of(ConfigReader.get("categoryImage")))
                .build();

    }

    public UpdateProductRequestDto updateProductWithWrongData() {
        return UpdateProductRequestDto.builder()
                .title(" ")
                .price(randomDataFactory.randomInt(0,1000))
                .description("description")
                .categoryId(-1)
                .images(List.of(ConfigReader.get("categoryImage")))
                .build();

    }


}
