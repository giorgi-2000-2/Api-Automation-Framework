package ge.gmikeladze.platzi.cleanup;
import lombok.Getter;
import java.util.Objects;
@Getter
public final class ResourceKey {
    public final static  String TYPE_CATEGORY = "CATEGORY";
    public final static  String TYPE_PRODUCT = "PRODUCT";
    public final static String TYPE_USER = "USER";
    private final String type;
    private final int id;

    public ResourceKey(String type, int id) {
        this.type = type;
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ResourceKey that = (ResourceKey) obj;

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