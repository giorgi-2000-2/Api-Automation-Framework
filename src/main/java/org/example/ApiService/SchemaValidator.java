package org.example.ApiService;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.asserts.SoftAssert;
import org.example.Utils.ExtentReportManager;
import java.lang.reflect.Field;
import java.util.Map;
public class SchemaValidator {
    public void assertResponseTypesMatchDTO(Response response, Class<?> dtoClass, SoftAssert softly) {
        JsonPath jsonPath = response.jsonPath();

        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().info("<b>[სქემის ვალიდაცია]</b> დაიწყო რესპონსის შედარება კლასთან: " + dtoClass.getSimpleName());
        }

        validateObject("", dtoClass, jsonPath.get(), softly);
    }

    private void validateObject(String pathPrefix, Class<?> dtoClass, Object actualData, SoftAssert softly) {
        if (actualData == null) return;

        if (actualData instanceof Map) {
            Map<?, ?> actualMap = (Map<?, ?>) actualData;
            Field[] fields = dtoClass.getDeclaredFields();

            for (Field field : fields) {
                String fieldName = field.getName();
                Class<?> expectedType = field.getType();
                Object fieldActualValue = actualMap.get(fieldName);

                String fullPath;
                if (pathPrefix.isEmpty()) {
                    fullPath = fieldName;
                } else {
                    fullPath = pathPrefix + "." + fieldName;
                }

                if (fieldActualValue == null) {
                    boolean hasKey = actualMap.containsKey(fieldName);
                    if (!hasKey) {
                        String errMsg = "ველი '" + fullPath + "' საერთოდ არ მოიძებნა API რესპონსში";
                        softly.assertTrue(false, errMsg);
                        logToExtent(false, errMsg);
                    }
                    continue;
                }

                Class<?> wrappedExpectedType = wrapPrimitive(expectedType);

                if (fieldActualValue instanceof Map) {
                    logToExtent(true, "• ველი (ობიექტი) <b>" + fullPath + "</b> ემთხვევა ტიპს -> შევდივართ შიდა ველების შესამოწმებლად.");
                    validateObject(fullPath, expectedType, fieldActualValue, softly);
                } else {

                    Class<?> actualType = fieldActualValue.getClass();
                    boolean isMatch = wrappedExpectedType.isAssignableFrom(actualType);

                    if (isMatch) {
                        logToExtent(true, " ველი: <b>" + fullPath + "</b> | მოვიდა: <code>" + actualType.getSimpleName() + "</code> (ემთხვევა DTO-ს)");
                    } else {
                        String errMsg = String.format(" ველის ტიპის შეუსაბამობა -> ველისთვის: '<b>%s</b>'. ველოდით: <code>%s</code>, მოვიდა: <code>%s</code>",
                        fullPath, wrappedExpectedType.getSimpleName(), actualType.getSimpleName());

                        softly.assertTrue(false, errMsg);
                        logToExtent(false, errMsg);
                    }
                }
            }
        }
    }


    private void logToExtent(boolean isSuccess, String message) {
        if (ExtentReportManager.getTest() != null) {
            if (isSuccess) {
                ExtentReportManager.getTest().info("<span style='color:green;'>" + message + "</span>");
            } else {
                ExtentReportManager.getTest().fail("<span style='color:red; font-weight:bold;'>" + message + "</span>");
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