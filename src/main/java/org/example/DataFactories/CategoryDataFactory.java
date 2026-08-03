package org.example.DataFactories;
import org.example.DTOs.RequestDto.CreateCategoryRequestDto;
import org.example.DTOs.RequestDto.GetCategoryLimitRequestDto;
import org.example.DTOs.RequestDto.UpdateCategoryRequestDto;
import org.example.Managers.FactoryManager;
import org.example.Utils.ConfigReader;

public class CategoryDataFactory {

    private final RandomDataFactory randomDataFactory = new RandomDataFactory();
    private final FactoryManager factoryManager;

    public CategoryDataFactory(FactoryManager factoryManager) {
        this.factoryManager = factoryManager;

    }

    public CreateCategoryRequestDto createCategoryWithData() {
        return CreateCategoryRequestDto.builder()
                .name(randomDataFactory.uniqueTitle(ConfigReader.get("category.name")))
                .image(((ConfigReader.get("category.image"))))
                .build();
    }

    public CreateCategoryRequestDto createCategoryWithWrongData() {
        return CreateCategoryRequestDto.builder()
                .name((" "))
                .image(ConfigReader.get("category.image"))
                .build();
    }

    public CreateCategoryRequestDto createCategoryWithWrongDataEmpty() {
        return CreateCategoryRequestDto.builder()
                .name("")
                .image("")
                .build();
    }

    public CreateCategoryRequestDto createDefaultCategory() {
        return CreateCategoryRequestDto.builder()
                .name("Default Name")
                .image(ConfigReader.get("category.image"))
                .build();
    }

    public GetCategoryLimitRequestDto getCategoryLimit() {
        return GetCategoryLimitRequestDto.builder()
                .limit(randomDataFactory.randomInt(ConfigReader.getInt("Limit")))
                .build();


    }

    public int getWrongCategoryId() {
        return randomDataFactory.randomInt(-1, 0);


    }

    public UpdateCategoryRequestDto updateCategoryDto() {
        return UpdateCategoryRequestDto.builder()
                .name(randomDataFactory.uniqueTitle(ConfigReader.get("category.name")))
                .image(((ConfigReader.get("category.image"))))
                .build();
    }

    public UpdateCategoryRequestDto updateCategoryDtoBadRequest() {
        return UpdateCategoryRequestDto.builder()
                .name(" ")
                .image(((ConfigReader.get("category.image"))))
                .build();
    }

    public String emptyField(){
        return " ";
    }


}