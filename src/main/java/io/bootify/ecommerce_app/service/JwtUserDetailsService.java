package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.model.JwtUserDetails;
import io.bootify.ecommerce_app.model.Role;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public JwtUserDetailsService(final AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public JwtUserDetails loadUserByUsername(final String username) {
        final AppUser appUser = appUserRepository.findByUsernameIgnoreCase(username);
        if (appUser == null) {
            log.warn("user not found: {}", username);
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        final String role = "vendor".equals(username) ? Role.VENDOR.name() : 
                ("admin".equals(username) ? Role.ADMIN.name() : Role.CUSTOMER.name());
        final List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        return new JwtUserDetails(appUser.getId(), username, appUser.getPassword(), authorities);
    }

}
