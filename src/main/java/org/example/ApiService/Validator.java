package org.example.ApiService;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.example.Utils.ExtentReportManager;
import org.testng.asserts.SoftAssert;
import java.lang.reflect.Field;
import java.util.Map;

public class Validator {


    public void ValidateJason(Response jason , Class<?> dtoclass,  SoftAssert softAssert ){
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().info("<b>[სქემის ვალიდაცია]</b> დაიწყო რესპონსის შედარება კლასთან: " + dtoclass.getSimpleName());
        }

        if(jason==null)return;

        validatorJasonResponseToDtoClass(jason,dtoclass,softAssert);
    }




    private void validatorJasonResponseToDtoClass(Response responseApi , Class<?> dtoclass, SoftAssert softAssert) {

        if (dtoclass == null) return;
        JsonPath jsonPath = responseApi.jsonPath();


        if (jsonPath.get() instanceof Map) {
            Field[] fields = dtoclass.getDeclaredFields();
            Map<?,?> mapJson = jsonPath.get();

            for (Field field : fields) {
                String fieldName = field.getName();
                Class<?> fieldType = field.getType();
                Object jsonFieldType = mapJson.get(fieldName);

                if (jsonFieldType != null && jsonFieldType.getClass() == fieldType) {
                    ExtentReportManager.getTest().pass(" ტესტში წარმატებულად შემოწმდა ფილდის ტიპი");
                    softAssert.assertEquals(jsonFieldType.getClass(), fieldType);

                }

            }

        }
    }



}

