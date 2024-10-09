package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.model.RegistrationRequest;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(final AppUserRepository appUserRepository,
            final PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(final RegistrationRequest registrationRequest) {
        log.info("registering new user: {}", registrationRequest.getUsername());

        final AppUser appUser = new AppUser();
        appUser.setUsername(registrationRequest.getUsername());
        appUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        appUser.setFullname(registrationRequest.getFullname());
        appUser.setRole(registrationRequest.getRole());
        appUserRepository.save(appUser);
    }

}
