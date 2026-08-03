package org.example.DTOs.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PutBadRequestResponse {
    private   String path;
    private   String timestamp;
    private   String name;
    private   String message;
    private   String code;

}