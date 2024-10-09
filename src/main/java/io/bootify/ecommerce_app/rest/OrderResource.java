package io.bootify.ecommerce_app.rest;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.model.CreateOrderRequest;
import io.bootify.ecommerce_app.model.OrderDTO;
import io.bootify.ecommerce_app.model.OrderStatus;
import io.bootify.ecommerce_app.model.Role;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.service.OrderService;
import io.bootify.ecommerce_app.util.CustomCollectors;
import io.bootify.ecommerce_app.util.ReferencedException;
import io.bootify.ecommerce_app.util.ReferencedWarning;
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
@RequestMapping(value = "/api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + Role.Fields.CUSTOMER + "', '" + Role.Fields.VENDOR + "', '" + Role.Fields.ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class OrderResource {

    @Autowired
    private OrderService orderService;


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable(name = "orderId") final Long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid final CreateOrderRequest createOrderRequest) {
        return new ResponseEntity<>(orderService.createOrder(createOrderRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        return ResponseEntity.ok(orderService.getUserOrders());
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable(name = "orderId") final Long orderId,
        @RequestParam(name = "status") final OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

}
