package ge.gmikeladze.platzi.dtos.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequestDto {
    private String title;
    private Integer price;
    private String description;
    private Integer categoryId;
    private List<String> images;

}