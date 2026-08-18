package ge.gmikeladze.platzi.dtos.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationErrorDto implements ApiError {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> message;
    private String error;
    private Integer statusCode;

    @Override
    public List<String> messages() {
        if (this.message == null) {
            return List.of();
        } else {
            return this.message;
        }
    }



}