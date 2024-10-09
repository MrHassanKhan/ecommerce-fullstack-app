package io.bootify.ecommerce_app.repos;

import io.bootify.ecommerce_app.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);
    boolean existsById(Long id);

}
