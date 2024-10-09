package io.bootify.ecommerce_app.service;

import io.bootify.ecommerce_app.domain.AppUser;
import io.bootify.ecommerce_app.domain.Order;
import io.bootify.ecommerce_app.domain.Payment;
import io.bootify.ecommerce_app.model.PaymentDTO;
import io.bootify.ecommerce_app.model.PaymentStatus;
import io.bootify.ecommerce_app.repos.AppUserRepository;
import io.bootify.ecommerce_app.repos.OrderRepository;
import io.bootify.ecommerce_app.repos.PaymentRepository;
import io.bootify.ecommerce_app.util.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

//    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final AppUserService appUserService;

    public PaymentService(final PaymentRepository paymentRepository,
            final AppUserService appUserService, final OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.appUserService = appUserService;
//        Stripe.apiKey = stripeSecretKey; // Initialize Stripe with the secret key
    }


    public String createPaymentIntent(PaymentDTO paymentDTO) {
        // Create a PaymentIntent with Stripe
        Map<String, Object> params = new HashMap<>();
        params.put("amount", (int) (paymentDTO.getAmount() * 100)); // amount in cents
        params.put("currency", paymentDTO.getCurrency());

//        PaymentIntent intent = PaymentIntent.create(params);

        // Save PaymentIntent ID in the Order entity for future reference
        Order order = orderRepository.findById(paymentDTO.getOrderId()).orElseThrow();
//        order.setStripePaymentIntentId(intent.getId());
        orderRepository.save(order);

//        return intent.getClientSecret(); // return the client secret to frontend


        return "";
    }
    public Payment confirmPayment(String paymentIntentId) {
//        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
//        PaymentIntent confirmedIntent = intent.confirm();
//
//        if ("succeeded".equals(confirmedIntent.getStatus())) {
//            // Update order status
//            Order order = orderRepository.findByStripePaymentIntentId(paymentIntentId).orElseThrow();
//            order.setStatus("PAID");
//            orderRepository.save(order);
//
//            // Create Payment entity
//            Payment payment = new Payment();
//            payment.setOrder(order);
//            payment.setAmount(order.getTotalAmount());
//            payment.setPaymentStatus("SUCCESS");
//            return payment;
//        } else {
//            throw new IllegalStateException("Payment not successful");
//        }
        return null;
    }


//    public List<PaymentDTO> findAll() {
//        final List<Payment> payments = paymentRepository.findAll(Sort.by("id"));
//        return payments.stream()
//                .map(payment -> mapToDTO(payment, new PaymentDTO()))
//                .toList();
//    }

//    public PaymentDTO get(final Long id) {
//        return paymentRepository.findById(id)
//                .map(payment -> mapToDTO(payment, new PaymentDTO()))
//                .orElseThrow(NotFoundException::new);
//    }



//    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
//        paymentDTO.setId(payment.getId());
//        paymentDTO.setAmount(payment.getAmount());
//        paymentDTO.setStatus(payment.getStatus());
//        paymentDTO.setUserFullName(payment.getAppUser() == null ? null : payment.getAppUser().getFullname());
//        paymentDTO.setOrderId(payment.getOrder() == null ? null : payment.getOrder().getId());
//        return paymentDTO;
//    }
//
//    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
//        payment.setAmount(paymentDTO.getAmount());
//        payment.setStatus(paymentDTO.getStatus());
//        payment.setAppUser(appUserService.getAuthenticatedUser());
//        final Order order = paymentDTO.getOrderId() == null ? null : orderRepository.findById(paymentDTO.getOrderId())
//                .orElseThrow(() -> new NotFoundException("order not found"));
//        payment.setOrder(order);
//        return payment;
//    }

}
