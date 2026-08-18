package ge.gmikeladze.platzi.cleanup;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
@Getter
public final class ResourceKey {
    public static  String TYPE_CATEGORY = "CATEGORY";
    public static  String TYPE_PRODUCT = "PRODUCT";
    public static String TYPE_USER = "USER";
    private String type;
    private int id;


    public ResourceKey(String type, int id) {
        this.type = type;
        this.id = id;
    }




    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceKey that = (ResourceKey) o;

        return id == that.id && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }


    @Override
    public String toString() {
        return type + "#" + id;
    }
}