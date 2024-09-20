package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import shop.biday.model.domain.BidModel;
import shop.biday.model.dto.BidResponse;
import shop.biday.service.BidService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
@Tag(name = "bids", description = "Bid Controller")
public class BidController {

    private final Map<Long, Sinks.Many<BidResponse>> bidSinks = new ConcurrentHashMap<>();

    private final BidService bidService;

    @Operation(summary = "입찰 조회", description = "auctionId로 최고 입찰가를 조회합니다.(SSE)")
    @Parameters({
            @Parameter(name = "auctionId", description = "경매 ID", example = "1"),
    })
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BidResponse> streamBid(@RequestParam Long auctionId) {
        log.info("stream auctionId: {}", auctionId);
        Sinks.Many<BidResponse> bidSink = bidSinks.computeIfAbsent(auctionId, id ->
                Sinks.many().multicast().onBackpressureBuffer());
        return bidSink.asFlux().log();
    }

    @Operation(summary = "입찰 저장", description = "입찰 데이터를 저장합니다.")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping
    public Mono<BidResponse> save(@RequestBody BidModel bidModel) {
        log.info("save bidModel: {}", bidModel);
        return bidService.save(bidModel)
                .doOnNext(bid -> {
                    Sinks.Many<BidResponse> bidSink = bidSinks.get(bid.getAuctionId());
                    if (bidSink != null) {
                        bidSink.tryEmitNext(bid);
                    }
                });
    }
}
