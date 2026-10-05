package ge.gmikeladze.platzi.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokensDto {
    private String access_token;
    private String refresh_token;
}