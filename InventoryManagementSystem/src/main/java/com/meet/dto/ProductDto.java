package com.meet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long id;

    private Long productId;
    private Long supplierId;
    private Long categoryId;
    private String name;
    private String sku;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private Integer stockQuantity;
    private LocalDateTime expiryDate;
    private LocalDateTime updatedAt;
    private final LocalDateTime createdAt= LocalDateTime.now();
}
