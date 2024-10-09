package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Cart;
import io.bootify.ecommerce_app.domain.CartItem;
import io.bootify.ecommerce_app.domain.Product;
import io.bootify.ecommerce_app.model.CartDTO;
import io.bootify.ecommerce_app.model.CartItemDTO;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.repos.CartRepository;
import io.bootify.ecommerce_app.repos.ProductRepository;
import io.bootify.ecommerce_app.util.NotFoundException;
import io.bootify.ecommerce_app.util.ReferencedWarning;
import java.util.List;

import io.bootify.ecommerce_app.util.SecurityAppUser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AppUserService appUserService;

    @Value("${project.url}")
    private String baseUrl;

    public CartService(final CartRepository cartRepository, final ProductRepository productRepository, final AppUserService appUserService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.appUserService = appUserService;
    }

    public List<CartDTO> findAll() {
        final List<Cart> carts = cartRepository.findAll(Sort.by("id"));
        return carts.stream()
                .map(cart -> mapToDTO(cart, new CartDTO()))
                .toList();
    }


    @Transactional
    public CartDTO get(final Long userId) {
        Cart cart = cartRepository.findByAppUserId(userId).orElseGet(() -> createCart(userId));
        return mapToDTO(cart, new CartDTO());
    }

    @Transactional
    public CartDTO addToCart(final Long userId, final Long productId, final int quantity) {
        Cart cart = cartRepository.findByAppUserId(userId).orElseGet(() -> createCart(userId));

        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new NotFoundException("Product not found"));

        CartItem cartItem = cart.getCartItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().orElseGet(() -> {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            cart.getCartItems().add(item);
            return item;
        });

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cart.setTotalAmount(cart.getCartItems().stream().mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice()).sum());

        return mapToDTO(cart, new CartDTO());
    }

    @Transactional
    public CartDTO removeFromCart(final Long userId, final Long productId) {
        Cart cart = cartRepository.findByAppUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        cart.getCartItems().removeIf(item -> item.getProduct().getId().equals(productId));
        cart.setTotalAmount(cart.getCartItems().stream().mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice()).sum());
        cartRepository.save(cart);
        return mapToDTO(cart, new CartDTO());
    }

    @Transactional
    public CartDTO clearCart(final Long userId) {
        Cart cart = cartRepository.findByAppUserId(userId).orElseThrow(() -> new NotFoundException("Cart not found"));
        cart.getCartItems().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);
        return mapToDTO(cart, new CartDTO());
    }



    private Cart createCart(Long userId) {
        Cart cart = new Cart();
        cart.setAppUser(appUserService.getAuthenticatedUser());
        return cartRepository.save(cart);
    }

    private CartDTO mapToDTO(final Cart cart, final CartDTO cartDTO) {
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getAppUser().getId());
        cartDTO.setTotalAmount(cart.getTotalAmount());
        if(cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            return cartDTO;
        }
        cartDTO.setCartItems(cart.getCartItems().stream().map(cartItem -> mapToDTO(cartItem, new CartItemDTO())).toList());
        return cartDTO;
    }

    private CartItemDTO mapToDTO(final CartItem cartItem, final CartItemDTO cartItemDTO) {
        cartItemDTO.setId(cartItem.getId());
        cartItemDTO.setPrice(cartItem.getProduct().getPrice());
        cartItemDTO.setProductId(cartItem.getProduct().getId());
        cartItemDTO.setProductName(cartItem.getProduct().getName());
        cartItemDTO.setImageUrl(cartItem.getProduct().getImageUrl() == null ? null : baseUrl + "/file/" + cartItem.getProduct().getImageUrl());
        cartItemDTO.setQuantity(cartItem.getQuantity());
        return cartItemDTO;
    }

}
