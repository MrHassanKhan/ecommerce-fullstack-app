package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.*;
import io.bootify.ecommerce_app.model.*;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.repos.OrderRepository;
import io.bootify.ecommerce_app.repos.PaymentRepository;
import io.bootify.ecommerce_app.repos.ProductRepository;
import io.bootify.ecommerce_app.util.NotFoundException;
import io.bootify.ecommerce_app.util.ReferencedWarning;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import io.bootify.ecommerce_app.util.SecurityAppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private AppUserService appUserService;


    @Transactional
    public OrderDTO createOrder(CreateOrderRequest createOrderRequest) {
        CartDTO cartDTO = cartService.get(SecurityAppUser.getAuthenticatedUserId());
        if(cartDTO.getCartItems().isEmpty()) {
            throw new NotFoundException("Cart is empty");
        }

        Order order = new Order();
        order.setAppUser(appUserService.getAuthenticatedUser());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(createOrderRequest.getShippingAddress());
        order.setShippingCity(createOrderRequest.getShippingCity());
        order.setShippingState(createOrderRequest.getShippingState());
        order.setShippingZip(createOrderRequest.getShippingZip());
        order.setShippingCountry(createOrderRequest.getShippingCountry());
        Double totalAmount = Double.valueOf(0);

        for (CartItemDTO cartItem : cartDTO.getCartItems()) {
            Product product = productRepository.findById(cartItem.getProductId()).orElseThrow();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            order.getItems().add(orderItem);
            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        order.setCreatedDate(OffsetDateTime.now(ZoneOffset.UTC));
        orderRepository.save(order);
        cartService.clearCart(SecurityAppUser.getAuthenticatedUserId());
        return convertToDto(order);
    }

    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long orderId) {
        return convertToDto(orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found")));
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getUserOrders() {
        return orderRepository.findByAppUserId(SecurityAppUser.getAuthenticatedUserId()).stream().map(this::convertToDto).toList();
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
        return convertToDto(order);
    }


    private OrderDTO convertToDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setStripePaymentIntentId(order.getStripePaymentIntentId());
        orderDTO.setShippingAddress(order.getShippingAddress());
        orderDTO.setShippingCity(order.getShippingCity());
        orderDTO.setShippingState(order.getShippingState());
        orderDTO.setShippingZip(order.getShippingZip());
        orderDTO.setShippingCountry(order.getShippingCountry());
        orderDTO.setItems(order.getItems().stream().map(this::convertToDTO).toList());
        return orderDTO;
    }

    private OrderItemDTO convertToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setId(orderItem.getId());
        orderItemDTO.setProductId(orderItem.getProduct().getId());
        orderItemDTO.setProductName(orderItem.getProduct().getName());
        orderItemDTO.setQuantity(orderItem.getQuantity());
        orderItemDTO.setPrice(orderItem.getPrice());
        return orderItemDTO;
    }

}
