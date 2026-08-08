package ge.gmikeladze.platzi.apiclient;

public enum ApiEndpoint {

    CATEGORY("/api/v1/categories"),
    CATEGORY_ID("/api/v1/categories/{id}"),
    CATEGORY_SLUG("/api/v1/categories/slug/{slug}"),
    PRODUCT("/api/v1/products"),
    PRODUCT_ID("/api/v1/products/{id}"),
    CATEGORY_ID_PRODUCTS("/api/v1/categories/{id}/products");


    private final String path;

    ApiEndpoint(String path) { this.path = path; }

    public String path() { return path; }
}