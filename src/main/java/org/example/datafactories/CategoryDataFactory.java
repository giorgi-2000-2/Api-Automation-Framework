package org.example.datafactories;
import org.example.dtos.requestdto.CreateCategoryRequestDto;
import org.example.dtos.requestdto.GetCategoryLimitRequestDto;
import org.example.dtos.requestdto.UpdateCategoryRequestDto;
import org.example.utils.ConfigReader;

public class CategoryDataFactory {

    private RandomDataFactory randomDataFactory;

    public CategoryDataFactory(RandomDataFactory randomDataFactory) {
        this.randomDataFactory = randomDataFactory;

    }

    public CreateCategoryRequestDto createCategoryWithData() {
        return CreateCategoryRequestDto.builder()
                .name(randomDataFactory.uniqueTitle(ConfigReader.get("categoryName")))
                .image(((ConfigReader.get("categoryImage"))))
                .build();
    }

    public CreateCategoryRequestDto createCategoryWithWrongData() {
        return CreateCategoryRequestDto.builder()
                .name((" "))
                .image(ConfigReader.get("categoryImage"))
                .build();
    }

    public CreateCategoryRequestDto createCategoryWithWrongDataEmpty() {
        return CreateCategoryRequestDto.builder()
                .name("")
                .image("")
                .build();
    }


    public GetCategoryLimitRequestDto getCategoryLimit() {
        return GetCategoryLimitRequestDto.builder()
                .limit(randomDataFactory.randomInt(1,ConfigReader.getInt("Limit")))
                .build();


    }


    public UpdateCategoryRequestDto updateCategoryDto() {
        return UpdateCategoryRequestDto.builder()
                .name(randomDataFactory.uniqueTitle(ConfigReader.get("categoryName")))
                .image(((ConfigReader.get("categoryImage"))))
                .build();
    }

    public UpdateCategoryRequestDto updateCategoryDtoBadRequest() {
        return UpdateCategoryRequestDto.builder()
                .name(" ")
                .image(((ConfigReader.get("categoryImage"))))
                .build();
    }

    public String emptyField(){
        return " ";
    }


}