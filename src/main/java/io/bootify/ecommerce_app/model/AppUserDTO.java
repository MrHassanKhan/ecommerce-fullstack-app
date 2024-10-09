package io.bootify.ecommerce_app.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppUserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @AppUserUsernameUnique
    private String username;

    @NotNull
    @Size(max = 255)
    private String password;

    @NotNull
    @Size(max = 255)
    private String fullname;

    @NotNull
    private Role role;

}
