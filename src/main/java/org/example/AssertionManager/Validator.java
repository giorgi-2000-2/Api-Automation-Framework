package org.example.AssertionManager;
import io.restassured.response.Response;
import org.example.Utils.ExtentReportManager;
import org.testng.asserts.SoftAssert;
import java.lang.reflect.Field;
import java.util.Map;

public class Validator {


    public void ValidateJson(Response jason , Class<?> dtoclass, SoftAssert softAssert ){
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().info("სქემის ვალიდაცია დაიწყო – რესპონსის შედარება კლასთან: " + dtoclass.getSimpleName());
        }

        if (jason == null || dtoclass == null) return;
        Object jsonBody = jason.jsonPath().get();
        if (jsonBody instanceof Map) {
            validatorJasonResponseToDtoClass(jsonBody, dtoclass, softAssert);
        } else {
            softAssert.fail("Response არ არის Map");
        }
    }

    private void validatorJasonResponseToDtoClass( Object mapJson1  , Class<?> dtoclass, SoftAssert softAssert) {
        Field[] fields = dtoclass.getDeclaredFields();
        Map<?, ?> mapJson = (Map<?, ?>) mapJson1;

        softAssert.assertEquals(fields.length, mapJson.size()," field ის რაოდენობა jason –ში არის - "+ mapJson.size() + " და  DTO კლასში  – "+ fields.length);
        ExtentReportManager.getTest().info( " field ის რაოდენობა jason –ში არის - "+ mapJson.size() + " და  DTO კლასში  – "+ fields.length);

        for (Field field : fields) {

            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            Object jsonFieldType = mapJson.get(fieldName);
            if (jsonFieldType == null) {
                ExtentReportManager.getTest().info("ველი '" + fieldName + "' არის null, გამოტოვება");
                continue;
            }
            if (jsonFieldType instanceof Map) {
                validatorJasonResponseToDtoClass(jsonFieldType, fieldType, softAssert);
                continue;
            }
            fieldType=  wrapPrimitive(fieldType);
            softAssert.assertTrue(fieldType.isAssignableFrom(jsonFieldType.getClass()));
            if (fieldType.isAssignableFrom(jsonFieldType.getClass())) {
                ExtentReportManager.getTest().pass(" ტესტში წარმატებულად შემოწმდა ფილდის ტიპი" + fieldType +" და "+ jsonFieldType);

            }

        }

    }
    private Class<?> wrapPrimitive(Class<?> type) {
        if (!type.isPrimitive()) return type;
        if (type == int.class) return Integer.class;
        if (type == long.class) return Long.class;
        if (type == double.class) return Double.class;
        if (type == float.class) return Float.class;
        if (type == boolean.class) return Boolean.class;
        if (type == char.class) return Character.class;
        if (type == byte.class) return Byte.class;
        if (type == short.class) return Short.class;
        return type;
    }

}














