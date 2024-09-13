package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.BidModel;
import shop.biday.model.dto.BidDto;
import shop.biday.service.BidService;

import java.time.LocalDateTime;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
@Tag(name = "bids", description = "Bid Controller")
public class BidController {
    private final BidService bidService;

    @GetMapping // 강사님한테 api 어떻게 적어야할지 물어보기!!
    @Operation(summary = "입찰 목록", description = "마이 페이지에서 불러올 수 있는 입찰 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입찰 목록 가져오기 성공"),
            @ApiResponse( responseCode = "404", description = "입찰 목록 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "userId", description = "구매자 id", example = "1L"),
            @Parameter(name = "period", description = "기간별 정렬", example = "3개월"),
            @Parameter(name = "cursor", description = "현재 페이지에서 가장 마지막 입찰의 id", example = "1L"),
    })
    public Slice<BidDto> findByUser(@RequestParam(value = "userId", required = true) Long userId,
                                    @RequestParam(value = "period", required = false, defaultValue = "3개월") String period,
                                    @RequestParam(value = "cursor", required = false) LocalDateTime cursor,
                                    Pageable pageable) {
        return bidService.findByUser(userId, period, cursor, pageable);
    }

    @GetMapping("/findById")
    @Operation(summary = "입찰 찾기(=주문 목록)", description = "마이페이지에서 입찰 리스트 통해 이동 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "입찰 불러오기 성공"),
            @ApiResponse( responseCode = "404", description = "입찰 찾을 수 없음")
    })
    @Parameter(name = "id", description = "상세보기할 입찰의 id", example = "1L")
    public BidModel findById(@RequestParam(value = "id", required = true) Long id) {
        return bidService.findById(id);
    }
}
