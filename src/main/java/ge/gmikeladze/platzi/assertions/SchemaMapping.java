package ge.gmikeladze.platzi.assertions;

import ge.gmikeladze.platzi.dtos.response.BadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.GetResponseCategoryDto;
import ge.gmikeladze.platzi.dtos.response.GetResponseProductDto;
import ge.gmikeladze.platzi.dtos.response.PutBadRequestResponse;
import ge.gmikeladze.platzi.dtos.response.ValidationErrorDto;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO ↔ JSON Schema-ს რეგისტრი.
 *
 * FIX L-7 (dead code): წაშლილია სამი გამოუყენებელი ჩანაწერი და მათი სქემები —
 *   • DELETE_CATEGORY  (DeleteCategoryResponseDto  → 0 გამოყენება; წაშლა validateWithoutSchema-ზე მიდის)
 *   • ERROR_RESPONSE   (ErrorResponseDto           → 0 გამოყენება; ValidationErrorDto იმავე კონტრაქტს ფარავს)
 *   • PRODUCT_LIST     (GetResponseProductDto[]    → 0 გამოყენება; პროდუქტების სიის ტესტი ჯერ არ არსებობს)
 * ჩანაწერი აქ უნდა დაბრუნდეს მაშინ, როცა შესაბამისი ტესტი დაიწერება (YAGNI).
 */
public enum SchemaMapping {

    CATEGORY(GetResponseCategoryDto.class,        "schemas/category-success-schema.json"),
    CATEGORY_LIST(GetResponseCategoryDto[].class, "schemas/category-list-schema.json"),
    PRODUCT(GetResponseProductDto.class,          "schemas/product-schema.json"),
    BAD_REQUEST(BadRequestResponse.class,         "schemas/bad-request-schema.json"),
    PUT_BAD_REQUEST(PutBadRequestResponse.class,  "schemas/put-bad-request-schema.json"),
    VALIDATION_ERROR(ValidationErrorDto.class,    "schemas/validation-error-schema.json");

    private final Class<?> dtoClass;
    private final String schemaPath;

    SchemaMapping(Class<?> dtoClass, String schemaPath) {
        this.dtoClass = dtoClass;
        this.schemaPath = schemaPath;
    }

    private static final Map<Class<?>, String> INDEX = buildIndex();

    private static Map<Class<?>, String> buildIndex() {
        Map<Class<?>, String> index = new HashMap<>();

        for (SchemaMapping m : values()) {

            String previous = index.put(m.dtoClass, m.schemaPath);
            if (previous != null) {
                throw new IllegalStateException(
                        "SchemaMapping: " + m.dtoClass.getName() + " ორჯერაა რეგისტრირებული — \""
                                + previous + "\" და \"" + m.schemaPath + "\"");
            }

            if (SchemaMapping.class.getClassLoader().getResource(m.schemaPath) == null) {
                throw new IllegalStateException(
                        "SchemaMapping." + m.name() + ": ფაილი არ არსებობს classpath-ზე → " + m.schemaPath);
            }
        }
        return Map.copyOf(index);
    }

    public static String getPath(Class<?> dtoClass) {
        String path = INDEX.get(dtoClass);
        if (path == null) {
            throw new IllegalArgumentException(
                    "SchemaMapping: სქემა არ არის რეგისტრირებული → " + dtoClass.getName()
                            + ". დაამატე ჩანაწერი SchemaMapping enum-ში.");
        }
        return path;
    }
}
