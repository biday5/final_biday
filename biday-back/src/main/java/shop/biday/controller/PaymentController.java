package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.PaymentTempModel;
import shop.biday.model.dto.PaymentRequest;
import shop.biday.model.dto.PaymentResponse;
import shop.biday.service.PaymentService;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Tag(name = "payments", description = "Payment Controller")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 데이터 임시 저장", description = "결제 요청 전 데이터 임시 저장합니다.")
    @ApiResponse(responseCode = "201", description = "성공")
    @PostMapping("/temp")
    public ResponseEntity<?> savePaymentTemp(@RequestBody @Validated PaymentTempModel paymentTempModel) {
        log.info("paymentTempModel: {}", paymentTempModel);
        paymentService.savePaymentTemp(paymentTempModel);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "결제 승인", description = "결제 승인 데이터 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 승인 성공"),
            @ApiResponse(responseCode = "400", description = "결제 승인 실패")
    })
    @PostMapping
    public ResponseEntity<?> savePayment(@RequestBody @Validated PaymentRequest paymentRequest) {
        log.info("paymentRequest: {}", paymentRequest);
        return new ResponseEntity<>(paymentService.save(paymentRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "결제 조회", description = "paymentKey로 결제 조회합니다.")
    @Parameters({
            @Parameter(name = "id", description = "결제 ID", example = "1"),
    })
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping
    public ResponseEntity<PaymentResponse> findPaymentByPaymentKey(@RequestParam("id") Long id) {
        log.info("findPaymentByPaymentKey id: {}", id);
        return new ResponseEntity<>(paymentService.findPaymentByPaymentKey(id), HttpStatus.OK);
    }

    @Operation(summary = "사용자 기준 결제 내역 조회", description = "userId로 결제 조회합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/findByUser")
    public ResponseEntity<List<PaymentRequest>> findByUser(@RequestHeader("Authorization") String token) {
        log.info("findByUser: {}", token);
        return new ResponseEntity<>(paymentService.findByUser(token), HttpStatus.OK);
    }
}
