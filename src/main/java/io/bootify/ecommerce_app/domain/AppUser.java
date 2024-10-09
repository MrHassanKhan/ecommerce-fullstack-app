package io.bootify.ecommerce_app.domain;

import io.bootify.ecommerce_app.model.Role;
import jakarta.persistence.*;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class AppUser {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false, name = "\"role\"")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "addedby", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Product> products;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Order> orders;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Payment> payments;

    @OneToMany(mappedBy = "appUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Cart> carts;

}
