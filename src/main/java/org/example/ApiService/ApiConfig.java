package org.example.ApiService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.example.Utils.ConfigReader;
import org.example.Utils.LogFilter;

public class ApiConfig {

    private static final ThreadLocal<RequestSpecification> threadLocalSpec = ThreadLocal.withInitial(() -> {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigReader.get("BASE_URL"))
                .setContentType("application/json")
                .addFilter(new LogFilter())
                .build();
    });

    private ApiConfig() {}

    static RequestSpecification base() {   // package-private — მხოლოდ ApiRequest ხედავს
        return threadLocalSpec.get();
    }

    public static void clearSpec() {
        threadLocalSpec.remove();
    }

}