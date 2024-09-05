package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.RefundService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/refunds")
@Tag(name = "refunds", description = "Refund Controller")
public class RefundController {
    private final RefundService refundService;
}
