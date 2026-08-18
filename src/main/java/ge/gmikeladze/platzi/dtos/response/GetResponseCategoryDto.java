package ge.gmikeladze.platzi.dtos.response;
import ge.gmikeladze.platzi.dtos.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetResponseCategoryDto implements Identifiable {
    private Integer id;
    private String name;
    private String slug;
    private String image;
    private String creationAt;
    private String updatedAt;
}

