package ge.gmikeladze.platzi.datafactories;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.dtos.request.CreateCategoryRequestDto;
import ge.gmikeladze.platzi.dtos.request.GetCategoryLimitRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateCategoryRequestDto;
import ge.gmikeladze.platzi.utils.config.IConfigForData;

@Singleton
public class CategoryDataFactory {
    private final RandomDataFactory randomDataFactory;
    private final IConfigForData config;
    @Inject
    public CategoryDataFactory(RandomDataFactory randomDataFactory, IConfigForData config) {
        this.randomDataFactory = randomDataFactory;
        this.config = config;
    }

    public CreateCategoryRequestDto createCategoryWithData() {
        return CreateCategoryRequestDto.builder()
                .name(randomDataFactory.uniqueTitle(config.categoryName()))
                .image(config.categoryImage())
                .build();
    }

    public GetCategoryLimitRequestDto getCategoryLimit() {
        return GetCategoryLimitRequestDto.builder()
                .limit(randomDataFactory.randomInt(1, config.categoryListLimit()))
                .build();
    }

    public UpdateCategoryRequestDto updateCategoryDto() {
        return UpdateCategoryRequestDto.builder()
                .name(randomDataFactory.uniqueTitle(config.categoryName()))
                .image(config.categoryImage())
                .build();
    }
}