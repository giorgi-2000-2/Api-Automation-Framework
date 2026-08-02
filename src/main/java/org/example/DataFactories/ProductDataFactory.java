package org.example.DataFactories;
import org.example.DTOs.RequestDto.CreateProductRequestDto;
import org.example.DTOs.RequestDto.UpdateProductRequestDto;
import org.example.DTOs.ResponseDto.GetResponseCategoryDTO;
import org.example.Managers.FactoryManager;
import org.example.Utils.ConfigReader;

import java.util.List;

public class ProductDataFactory {
    RandomDataFactory randomDataFactory = new RandomDataFactory();
    private FactoryManager factoryManager;

    public ProductDataFactory(FactoryManager factoryManager) {
        this.factoryManager = factoryManager;

    }

    public CreateProductRequestDto createProductWithData(int id) {
        return CreateProductRequestDto.builder()
                .title(randomDataFactory.uniqueTitle(ConfigReader.get("category.name")))
                .price(randomDataFactory.randomInt(1,100))
                .description(randomDataFactory.uniqueTitle("description"))
                .categoryId(id)
                .images(List.of(ConfigReader.get("category.image")))
                .build();
    }

    public int getWrongId() {
        return randomDataFactory.randomInt(-1, 0);


    }

    public UpdateProductRequestDto updateProductDto(GetResponseCategoryDTO response) {
        return UpdateProductRequestDto.builder()
                .title(randomDataFactory.uniqueTitle("updatedName"))
                .price(randomDataFactory.randomInt(0,1000))
                .description("description")
                .categoryId(response.getId())
                .images(List.of(ConfigReader.get("category.image")))
                .build();

    }

    public UpdateProductRequestDto updateProductWithWrongData() {
        return UpdateProductRequestDto.builder()
                .title(" ")
                .price(randomDataFactory.randomInt(0,1000))
                .description("description")
                .categoryId(-1)
                .images(List.of(ConfigReader.get("category.image")))
                .build();

    }


}
