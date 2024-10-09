package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Cart;
import io.bootify.ecommerce_app.domain.Order;
import io.bootify.ecommerce_app.domain.Payment;
import io.bootify.ecommerce_app.domain.Product;
import io.bootify.ecommerce_app.model.AppUserDTO;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.repos.CartRepository;
import io.bootify.ecommerce_app.repos.OrderRepository;
import io.bootify.ecommerce_app.repos.PaymentRepository;
import io.bootify.ecommerce_app.repos.ProductRepository;
import io.bootify.ecommerce_app.util.NotFoundException;
import io.bootify.ecommerce_app.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(final AppUserRepository appUserRepository,
            final PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AppUserDTO> findAll() {
        final List<AppUser> appUsers = appUserRepository.findAll(Sort.by("id"));
        return appUsers.stream()
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .toList();
    }

    public AppUserDTO get(final Long id) {
        return appUserRepository.findById(id)
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AppUserDTO appUserDTO) {
        if (appUserRepository.existsByUsernameIgnoreCase(appUserDTO.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }
        final AppUser appUser = new AppUser();
        mapToEntity(appUserDTO, appUser);
        return appUserRepository.save(appUser).getId();
    }

    public void update(final Long id, final AppUserDTO appUserDTO) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(appUserDTO, appUser);
        appUserRepository.save(appUser);
    }

    public void delete(final Long id) {
        if(!appUserRepository.existsById(id)) {
            throw new NotFoundException("AppUser not found with id: " + id);
        }
        appUserRepository.deleteById(id);
    }

    private AppUserDTO mapToDTO(final AppUser appUser, final AppUserDTO appUserDTO) {
        appUserDTO.setId(appUser.getId());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setFullname(appUser.getFullname());
        appUserDTO.setRole(appUser.getRole());
        return appUserDTO;
    }

    private AppUser mapToEntity(final AppUserDTO appUserDTO, final AppUser appUser) {
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        appUser.setFullname(appUserDTO.getFullname());
        appUser.setRole(appUserDTO.getRole());
        return appUser;
    }

    public boolean usernameExists(final String username) {
        return appUserRepository.existsByUsernameIgnoreCase(username);
    }

    public AppUser getAuthenticatedUser() {
        return appUserRepository.findByUsernameIgnoreCase(SecurityContextHolder.getContext().getAuthentication().getName());
    }


//    public ReferencedWarning getReferencedWarning(final Long id) {
//        final ReferencedWarning referencedWarning = new ReferencedWarning();
//        final AppUser appUser = appUserRepository.findById(id)
//                .orElseThrow(NotFoundException::new);
//        final Product addedbyProduct = productRepository.findFirstByAddedby(appUser);
//        if (addedbyProduct != null) {
//            referencedWarning.setKey("appUser.product.addedby.referenced");
//            referencedWarning.addParam(addedbyProduct.getId());
//            return referencedWarning;
//        }
//        final Order appUserOrder = orderRepository.findFirstByAppUser(appUser);
//        if (appUserOrder != null) {
//            referencedWarning.setKey("appUser.order.appUser.referenced");
//            referencedWarning.addParam(appUserOrder.getId());
//            return referencedWarning;
//        }
//        final Payment appUserPayment = paymentRepository.findFirstByAppUser(appUser);
//        if (appUserPayment != null) {
//            referencedWarning.setKey("appUser.payment.appUser.referenced");
//            referencedWarning.addParam(appUserPayment.getId());
//            return referencedWarning;
//        }
//        final Cart appUserCart = cartRepository.findFirstByAppUser(appUser);
//        if (appUserCart != null) {
//            referencedWarning.setKey("appUser.cart.appUser.referenced");
//            referencedWarning.addParam(appUserCart.getId());
//            return referencedWarning;
//        }
//        return null;
//    }

}
