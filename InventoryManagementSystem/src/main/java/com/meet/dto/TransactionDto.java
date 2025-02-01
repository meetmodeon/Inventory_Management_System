package com.meet.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.meet.enums.TransactionStatus;
import com.meet.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TransactionDto {
    private Long id;
    private Integer totalProducts;
    private BigDecimal totalPrice;
    private TransactionType transactionType;
    private TransactionStatus status;
    private String description;
    private LocalDateTime updatedAt;

    private UserDto user;
    private ProductDto product;
    private SupplierDto supplier;
}
