package ge.gmikeladze.platzi.apiclient;

import java.util.Map;
import java.util.Objects;

public class Pagination {

    private final int limit;
    private final int offset;

    public Pagination(int limit, int offset) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit არ შეიძლება იყოს უარყოფითი: " + limit);
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset არ შეიძლება იყოს უარყოფითი: " + offset);
        }
        this.limit = limit;
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagination that = (Pagination) o;
        return limit == that.limit && offset == that.offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, offset);
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}