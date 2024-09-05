package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.PaymentService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Tag(name = "payments", description = "Payment Controller")
public class PaymentController {
    private final PaymentService paymentService;
}
