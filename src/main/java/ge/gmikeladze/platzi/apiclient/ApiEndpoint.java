package ge.gmikeladze.platzi.apiclient;

public enum ApiEndpoint {

    CATEGORY("/api/v1/categories"),
    CATEGORY_ID("/api/v1/categories/{id}"),
    CATEGORY_SLUG("/api/v1/categories/slug/{slug}"),
    PRODUCT("/api/v1/products"),
    PRODUCT_ID("/api/v1/products/{id}"),
    CATEGORY_ID_PRODUCTS("/api/v1/categories/{id}/products"),
    USER("/api/v1/users"),
    USER_ID("/api/v1/users/{id}"),
    USER_IS_AVAILABLE("/api/v1/users/is-available"),
    AUTH_LOGIN("/api/v1/auth/login"),
    AUTH_PROFILE("/api/v1/auth/profile"),
    AUTH_REFRESH_TOKEN("/api/v1/auth/refresh-token");

    private final String path;

    ApiEndpoint(String path) { this.path = path; }

    public String path() { return path; }
}