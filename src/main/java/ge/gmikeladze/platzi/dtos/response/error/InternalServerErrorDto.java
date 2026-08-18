package ge.gmikeladze.platzi.dtos.response.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalServerErrorDto implements ApiError {

    private Integer statusCode;
    private String message;

    @Override
    public List<String> messages() {
        if (message == null || message.isBlank()) {
            return List.of();
        }
        return List.of(message);
    }


}