package io.bootify.ecommerce_app.rest;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.model.CartDTO;
import io.bootify.ecommerce_app.model.Role;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.service.CartService;
import io.bootify.ecommerce_app.util.CustomCollectors;
import io.bootify.ecommerce_app.util.ReferencedException;
import io.bootify.ecommerce_app.util.ReferencedWarning;
import io.bootify.ecommerce_app.util.SecurityAppUser;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/carts", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + Role.Fields.CUSTOMER + "', '" + Role.Fields.ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class CartResource {

    @Autowired
    CartService cartService;

//    @GetMapping
//    public ResponseEntity<List<CartDTO>> getAllCarts() {
//        return ResponseEntity.ok(cartService.findAll());
//    }

    @GetMapping
    public ResponseEntity<CartDTO> getCart() {
        return ResponseEntity.ok(cartService.get(SecurityAppUser.getAuthenticatedUserId()));
    }

    @PostMapping("/addToCart")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<CartDTO> addItemToCart(@RequestParam Long productId, @RequestParam int quantity) {
        final CartDTO cartDTO = cartService.addToCart(SecurityAppUser.getAuthenticatedUserId(), productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @PostMapping("/removeFromCart")
    public ResponseEntity<CartDTO> removeItemFromCart(@RequestParam Long productId) {
        final CartDTO cartDTO = cartService.removeFromCart(SecurityAppUser.getAuthenticatedUserId(), productId);
        return ResponseEntity.ok(cartDTO);
    }
    @PostMapping("/clearCart")
    public ResponseEntity<Void> clearCart() {
        cartService.clearCart(SecurityAppUser.getAuthenticatedUserId());
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("/{id}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deleteCart(@PathVariable(name = "id") final Long id) {
////        final ReferencedWarning referencedWarning = cartService.getReferencedWarning(id);
////        if (referencedWarning != null) {
////            throw new ReferencedException(referencedWarning);
////        }
//        cartService.delete(id);
//        return ResponseEntity.noContent().build();
//    }

}
