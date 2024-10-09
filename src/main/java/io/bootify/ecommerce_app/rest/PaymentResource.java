package io.bootify.ecommerce_app.rest;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Order;
import io.bootify.ecommerce_app.model.PaymentDTO;
import io.bootify.ecommerce_app.model.Role;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.repos.OrderRepository;
import io.bootify.ecommerce_app.service.PaymentService;
import io.bootify.ecommerce_app.util.CustomCollectors;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/payments", produces = MediaType.APPLICATION_JSON_VALUE)
@PreAuthorize("hasAnyAuthority('" + Role.Fields.CUSTOMER + "', '" + Role.Fields.VENDOR + "', '" + Role.Fields.ADMIN + "')")
@SecurityRequirement(name = "bearer-jwt")
public class PaymentResource {

    private final PaymentService paymentService;

    public PaymentResource(final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

//    @GetMapping
//    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
//        return ResponseEntity.ok(paymentService.findAll());
//    }


    @PostMapping("/processPayment")
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> processPayment(@RequestBody @Valid final PaymentDTO paymentDTO) {
//        try {
//            String clientSecret = paymentService.createPaymentIntent(paymentDTO);
//            return ResponseEntity.ok(clientSecret);
//        } catch (StripeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }

        return new ResponseEntity<>(1L, HttpStatus.CREATED);
    }
    @PostMapping("/confirm/{paymentIntentId}")
    public ResponseEntity<String> confirmPayment(@PathVariable String paymentIntentId) {
//        try {
//            paymentService.confirmPayment(paymentIntentId);
//            return ResponseEntity.ok("Payment confirmed successfully");
//        } catch (StripeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }

        return new ResponseEntity<>("Payment confirmed successfully", HttpStatus.OK);
    }

//    @DeleteMapping("/{id}")
//    @ApiResponse(responseCode = "204")
//    public ResponseEntity<Void> deletePayment(@PathVariable(name = "id") final Long id) {
//        paymentService.delete(id);
//        return ResponseEntity.noContent().build();
//    }

//    @GetMapping("/appUserValues")
//    public ResponseEntity<Map<Long, String>> getAppUserValues() {
//        return ResponseEntity.ok(appUserRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(AppUser::getId, AppUser::getUsername)));
//    }
//
//    @GetMapping("/orderValues")
//    public ResponseEntity<Map<Long, Long>> getOrderValues() {
//        return ResponseEntity.ok(orderRepository.findAll(Sort.by("id"))
//                .stream()
//                .collect(CustomCollectors.toSortedMap(Order::getId, Order::getId)));
//    }

}
