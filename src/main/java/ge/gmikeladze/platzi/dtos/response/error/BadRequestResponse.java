package ge.gmikeladze.platzi.dtos.response.error;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadRequestResponse implements ApiError {
  private   String path;
  private   String timestamp;
  private   String name;
  private   String message;

  @Override
  public List<String> messages() {
    if (this.message == null) {
      return List.of();
    } else {
      return List.of(this.message);
    }
  }

}