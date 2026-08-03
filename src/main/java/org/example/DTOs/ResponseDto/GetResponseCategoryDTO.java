package org.example.DTOs.ResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetResponseCategoryDTO {
    private Integer id;
    private String name;
    private String slug;
    private String image;
    private String creationAt;
    private String updatedAt;
}

