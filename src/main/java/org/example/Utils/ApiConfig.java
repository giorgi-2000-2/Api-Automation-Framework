package org.example.Utils;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class ApiConfig {

    private static final ThreadLocal<RequestSpecification> threadLocalSpec = ThreadLocal.withInitial(() -> {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("BASE_URL"))
                .setContentType("application/json")
                .addFilter(new LogFilter())
                .build();
    });


    public static RequestSpecification getBaseSpec() {
        return threadLocalSpec.get();
    }

    public static void clearSpec() {
        threadLocalSpec.remove();
    }

}