package ge.gmikeladze.platzi.datafactories;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import ge.gmikeladze.platzi.apiservice.HttpStatusCode;
import ge.gmikeladze.platzi.dtos.request.CreateProductRequestDto;
import ge.gmikeladze.platzi.dtos.request.UpdateProductRequestDto;
import ge.gmikeladze.platzi.dtos.response.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.InternalServerErrorDto;
import ge.gmikeladze.platzi.dtos.response.PutBadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.ValidationErrorDto;
import ge.gmikeladze.platzi.utils.ConfigReader;
import org.testng.annotations.DataProvider;
import java.util.Collections;
import java.util.List;
import java.util.function.IntFunction;
import static ge.gmikeladze.platzi.datafactories.NegativeCase.of;

@Singleton
public class ProductNegativeData{
    private final RandomDataFactory randomDataFactory;

    @Inject
    public ProductNegativeData(RandomDataFactory randomDataFactory) {
        this.randomDataFactory = randomDataFactory;
    }

    private final int VALID_PRICE = 100;
    private final String VALID_DESCRIPTION = "valid description";

    private String image() {
        return ConfigReader.get("categoryImage");
    }

    private String validTitle() {
        return randomDataFactory.uniqueTitle(ConfigReader.get("categoryName"));
    }

    private CreateProductRequestDto.CreateProductRequestDtoBuilder validCreate(int categoryId) {
        return CreateProductRequestDto.builder()
                .title(validTitle())
                .price(VALID_PRICE)
                .description(VALID_DESCRIPTION)
                .categoryId(categoryId)
                .images(List.of(image()));
    }

    private UpdateProductRequestDto.UpdateProductRequestDtoBuilder validUpdate(int categoryId) {
        return UpdateProductRequestDto.builder()
                .title(validTitle())
                .price(VALID_PRICE)
                .description(VALID_DESCRIPTION)
                .categoryId(categoryId)
                .images(List.of(image()));
    }


    @DataProvider(name = "invalidProductCreate")
    public Object[][] invalidProductCreate() {
        return new Object[][]{

                {of("price — უარყოფითი",
                        (IntFunction<CreateProductRequestDto>) id ->
                                validCreate(id).price(-10).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "price")},

                {of("price — 0",
                        (IntFunction<CreateProductRequestDto>) id ->
                                validCreate(id).price(0).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "price")},

                {of("categoryId = -1",
                        (IntFunction<CreateProductRequestDto>) id ->
                                validCreate(id).categoryId(-1).build(),
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class, "Category", "-1")},

                {of("categoryId = 0",
                        (IntFunction<CreateProductRequestDto>) id ->
                                validCreate(id).categoryId(0).build(),
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class, "Category")},

                {of("categoryId = Integer.MAX_VALUE (არარსებული)",
                        (IntFunction<CreateProductRequestDto>) id ->
                                validCreate(id).categoryId(Integer.MAX_VALUE).build(),
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class, "Category", "2147483647")},

                {of("images — null",
                        (IntFunction<CreateProductRequestDto>) id ->
                                validCreate(id).images(null).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "images")},

                {of("images — ცარიელი სია",
                        (IntFunction<CreateProductRequestDto>) id ->
                                validCreate(id).images(Collections.emptyList()).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "images")},

                {of("title — null ",
                        (IntFunction<CreateProductRequestDto>) id ->
                                validCreate(id).title(null).build(),
                        HttpStatusCode.SERVER_ERROR, InternalServerErrorDto.class, "Internal")},
        };
    }


    @DataProvider(name = "invalidProductUpdate")
    public Object[][] invalidProductUpdate() {
        return new Object[][]{

                {of("update price — უარყოფითი",
                        (IntFunction<UpdateProductRequestDto>) id ->
                                validUpdate(id).price(-20).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "price")},

                {of("update price — 0",
                        (IntFunction<UpdateProductRequestDto>) id ->
                                validUpdate(id).price(0).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "price")},

                {of("update images — null",
                        (IntFunction<UpdateProductRequestDto>) id ->
                                validUpdate(id).images(null).build(),
                        HttpStatusCode.SERVER_ERROR, InternalServerErrorDto.class, "Internal")},

                {of("update images — ცარიელი სია",
                        (IntFunction<UpdateProductRequestDto>) id ->
                                validUpdate(id).images(Collections.emptyList()).build(),
                        HttpStatusCode.BAD_REQUEST, ValidationErrorDto.class, "images")},

                {of("update title — null ",
                        (IntFunction<UpdateProductRequestDto>) id ->
                                validUpdate(id).title(null).build(),
                        HttpStatusCode.BAD_REQUEST, PutBadRequestResponse.class, "title")},
        };
    }

    @DataProvider(name = "invalidProductId")
    public Object[][] invalidProductId() {
        return new Object[][]{
                {of("id = 0", 0,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class, "Product")},

                {of("id = -1", -1,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class, "Product", "-1")},

                {of("id = Integer.MAX_VALUE (არარსებული)", Integer.MAX_VALUE,
                        HttpStatusCode.BAD_REQUEST, BadRequestResponse.class, "Product", "2147483647")},
        };
    }
}