package io.bootify.ecommerce_app.model;

import lombok.experimental.FieldNameConstants;


@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum Role {

    @FieldNameConstants.Include
    CUSTOMER,
    @FieldNameConstants.Include
    VENDOR,
    @FieldNameConstants.Include
    ADMIN

}
