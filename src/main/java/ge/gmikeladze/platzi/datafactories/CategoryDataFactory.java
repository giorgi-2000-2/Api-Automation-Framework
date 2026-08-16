package ge.gmikeladze.platzi.datafactories;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.GetCategoryLimitRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.utils.ConfigReader;

@Singleton
public class CategoryDataFactory {
    private final RandomDataFactory randomDataFactory;

    @Inject
    public CategoryDataFactory(RandomDataFactory randomDataFactory) {
        this.randomDataFactory = randomDataFactory;
    }

    public CreateCategoryRequestDto createCategoryWithData() {
        return CreateCategoryRequestDto.builder()
                .name(randomDataFactory.uniqueTitle(ConfigReader.get("categoryName")))
                .image((ConfigReader.get("categoryImage")))
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
                .image((ConfigReader.get("categoryImage")))
                .build();
    }





}