package io.bootify.ecommerce_app.model.requestDTO;

import lombok.Data;

import java.util.List;

@Data
public class ShopFilterDto {
    private List<Long> categorieIds;
    private List<Long> brandIds;
    private Double minPrice;
    private Double maxPrice;
}
