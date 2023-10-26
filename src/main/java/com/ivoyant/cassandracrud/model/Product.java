package com.ivoyant.cassandracrud.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table
public class Product {
    @PrimaryKey
    private Integer id;
    private String product;
    private Integer quantity;
    private Double price;
}
