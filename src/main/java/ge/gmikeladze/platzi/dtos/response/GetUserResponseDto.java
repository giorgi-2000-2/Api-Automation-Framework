package ge.gmikeladze.platzi.dtos.response;

import ge.gmikeladze.platzi.dtos.Identifiable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponseDto implements Identifiable {
    private Integer id;
    private String email;
    private String password;
    private String name;
    private String role;
    private String avatar;
    private String creationAt;
    private String updatedAt;
}