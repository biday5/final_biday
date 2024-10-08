package shop.biday.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;

public record BidModel(
        @Schema(description = "경매 ID", example = "1")
        @Positive
        @NotNull(message = "필수 값입니다.")
        Long auctionId,

        @Schema(description = "사용자 아이디", example = "1")
        @NotNull(message = "필수 값입니다.")
        String userId,

        @Schema(description = "입찰 가격", example = "1000")
        @Positive
        @NotNull(message = "필수 값입니다.")
        BigInteger currentBid) {
}
