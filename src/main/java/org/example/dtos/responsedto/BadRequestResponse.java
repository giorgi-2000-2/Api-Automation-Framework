package org.example.dtos.responsedto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadRequestResponse {
  private   String path;
  private   String timestamp;
  private String name;
  private   String message;


}