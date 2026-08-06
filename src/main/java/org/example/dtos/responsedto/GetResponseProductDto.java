package org.example.dtos.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetResponseProductDto {

    private Integer id;
    private String title;
    private String slug;
    private Integer price;
    private String description;
    private GetResponseCategoryDto category;
    private List<String> images;
    private String creationAt;
    private String updatedAt;

}