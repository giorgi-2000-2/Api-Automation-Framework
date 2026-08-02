package org.example.DTOs.RequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequestDto {
    private String title;
    private double price;
    private String description;
    private int categoryId;
    private List<String> images;
}