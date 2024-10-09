package io.bootify.ecommerce_app.model;

import java.util.Collection;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


/**
 * Extension of Spring Security User class to store additional data.
 */
public class JwtUserDetails extends User {

    @Getter
    final Long id;
    final String username;

    public JwtUserDetails(final Long id, final String username, final String hash,
            final Collection<? extends GrantedAuthority> authorities) {
        super(username, hash, authorities);
        this.id = id;
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
