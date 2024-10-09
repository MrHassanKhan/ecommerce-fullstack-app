package io.bootify.ecommerce_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandProductCountDTO {
    private Long id;
    private String name;
    private Long productCount;
}
