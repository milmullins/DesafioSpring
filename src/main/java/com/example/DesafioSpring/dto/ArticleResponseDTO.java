package com.example.DesafioSpring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDTO {
    private int productId;
    private String name;
    private String brand;
    private int quantity;
}
