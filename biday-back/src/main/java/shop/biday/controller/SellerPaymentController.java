package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.SellerPaymentService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellerPayments")
@Tag(name = "sellerPayments", description = "SellerPayment Controller")
public class SellerPaymentController {
    private final SellerPaymentService sellerPaymentService;
}
