package io.bootify.ecommerce_app.util;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.model.JwtUserDetails;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityAppUser {

//    public static AppUser getAuthenticatedUser() {
//        AppUser appUser = appUserRepository.findById(getAuthenticatedUserId())
//                .orElseThrow(IllegalArgumentException::new);
//
//        return appUser;
////        return appUserRepository.findByUsernameIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName());
//    }

    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof JwtUserDetails) {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
            return jwtUserDetails.getId();
        }
        throw new IllegalArgumentException("Authentication object is invalid or principal is not of type JwtUserDetails");
    }

//    private Long getUserIdFromAuthentication(Authentication authentication) {
//        // Ensure the authentication object is not null and the principal is of the expected type
//        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            return userDetails.getId();
//        }
//        // Handle case where authentication is invalid or principal is not of expected type
//        throw new IllegalArgumentException("Authentication object is invalid or principal is not of type CustomUserDetails");
//    }

}
