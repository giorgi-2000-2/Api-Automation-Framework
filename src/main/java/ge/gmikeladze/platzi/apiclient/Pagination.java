package ge.gmikeladze.platzi.apiclient;

import java.util.Map;

public record Pagination(int limit, int offset) {

    public Pagination {
        if (limit < 0) {
            throw new IllegalArgumentException("limit არ შეიძლება იყოს უარყოფითი: " + limit); }
        if (offset < 0) {
            throw new IllegalArgumentException("offset არ შეიძლება იყოს უარყოფითი: " + offset); }
    }

    public static Pagination of(int limit, int offset) {
        return new Pagination(limit, offset);
    }

    public static Pagination firstPage(int limit) {
        return new Pagination(limit, 0);
    }

    public static Pagination none() {
        return new Pagination(0, 0);
    }

    public Map<String, Object> asQueryParams() {
        return Map.of("limit", limit, "offset", offset);
    }
}