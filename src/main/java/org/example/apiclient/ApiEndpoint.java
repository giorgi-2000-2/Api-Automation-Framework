package org.example.apiclient;

public enum ApiEndpoint {

    CATEGORY("/api/v1/categories"),
    CATEGORY_ID("/api/v1/categories/{id}"),
    CATEGORY_SLUG("/api/v1/categories/slug/{slug}"),
    PRODUCT("/api/v1/products"),
    PRODUCT_ID("/api/v1/products/{id}");

    private final String path;

    ApiEndpoint(String path) { this.path = path; }

    public String path() { return path; }
}