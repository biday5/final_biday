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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.AuctionModel;
import shop.biday.model.dto.AuctionDto;
import shop.biday.model.entity.AuctionEntity;
import shop.biday.service.AuctionService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
@Tag(name = "auctions", description = "Auction Controller")
public class AuctionController {
    private final AuctionService auctionService;

    @GetMapping("/findById")
    @Operation(summary = "경매 상세보기", description = "경매 상세보기, 여기서는 경매와 해당 상품에 관한 정보만 가져옴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameter(name = "id", description = "상세보기할 경매의 id", example = "1")
    public ResponseEntity<AuctionModel> findById(@RequestParam(value = "id", required = true) Long id) {
        return ResponseEntity.ok(auctionService.findById(id));
    }

    @GetMapping("/findByProduct")
    @Operation(summary = "헤더 경매 목록", description = "종료 날짜에 따른 경매 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 목록 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "경매 목록 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "sizeId", description = "경매에 등록된 상품의 사이즈 id", example = "1"),
            @Parameter(name = "order", description = "정렬할 시간 기준", example = "종료 임박 순"),
            @Parameter(name = "cursor", description = "현재 페이지에서 가장 마지막 경매의 id", example = "1"),
    })
    public ResponseEntity<Slice<AuctionDto>> findByTime(
            @RequestParam(value = "sizeId", required = false) Long sizeId,
            @RequestParam(value = "order", required = false, defaultValue = "") String order,
            @RequestParam(value = "cursor", required = false) Long cursor,
            Pageable pageable) {
        return ResponseEntity.ok(auctionService.findByProduct(sizeId, order, cursor, pageable));
    }

    @GetMapping
    @Operation(summary = "마이페이지 경매 목록", description = "마이 페이지에서 불러올 수 있는 경매 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 목록 가져오기 성공"),
            @ApiResponse(responseCode = "404", description = "경매 목록 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "period", description = "기간별 정렬", example = "3개월"),
            @Parameter(name = "cursor", description = "현재 페이지에서 가장 마지막 경매의 id", example = "1"),
    })
    public ResponseEntity<Slice<AuctionDto>> findByUser(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "userId", required = true) String userId,
            @RequestParam(value = "period", required = false, defaultValue = "3개월") String period,
            @RequestParam(value = "cursor", required = false) Long cursor,
            Pageable pageable) {
        return ResponseEntity.ok(auctionService.findByUser(token, userId, period, cursor, pageable));
    }

    @PostMapping
    @Operation(summary = "경매 등록", description = "새로운 경매 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 등록 성공"),
            @ApiResponse(responseCode = "404", description = "경매 등록 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "userId", description = "판매자 userId", example = "66f1444c7415bc47b04b3478"),
            @Parameter(name = "product", description = "경매로 등록할 상품, product의 findById 사용해서 선택!", example = "1"),
            @Parameter(name = "description", description = "경매로 등록할 판매자 상품의 사진", example = "경매로 등록할 판매자 상품의 사진"),
            @Parameter(name = "startingBid", description = "경매 시작가, 상품 가격의 반값or40%로 시작", example = "50000"),
            @Parameter(name = "currentBid", description = "현재 경매가", example = "U23-TOP38-BR"),
            @Parameter(name = "startedAt", description = "시작 날짜", example = "localDateTime 값"),
            @Parameter(name = "endedAt", description = "종료 날짜", example = "localDateTime 값")
    })
    public ResponseEntity<AuctionEntity> save(@RequestHeader("Authorization") String token, @RequestBody AuctionModel auctionModel) {
        return ResponseEntity.ok(auctionService.save(token, auctionModel));
    }

    @PatchMapping
    @Operation(summary = "경매 수정", description = "진행 예정 경매 수정, 기존 시작 날짜 전에만 시작 날짜+끝나는 날짜만 변경 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "경매 수정 성공"),
            @ApiResponse(responseCode = "404", description = "경매 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "userId", description = "변경 불가, 판매자 userId", example = "66f1444c7415bc47b04b3478"),
            @Parameter(name = "product", description = "변경 불가, 경매로 등록할 상품, product의 findById 사용해서 선택!", example = "1"),
            @Parameter(name = "description", description = "변경 불가, 경매로 등록할 판매자 상품의 사진", example = "경매로 등록할 판매자 상품의 사진"),
            @Parameter(name = "startingBid", description = "변경 불가, 경매 시작가, 상품 가격의 반값or40%로 시작", example = "50000"),
            @Parameter(name = "currentBid", description = "변경 불가, 현재 경매가", example = "U23-TOP38-BR"),
            @Parameter(name = "startedAt", description = "만약 이미 시작되었다면 변경 불가, 시작 날짜", example = "localdatetime 값"),
            @Parameter(name = "endedAt", description = "만약 이미 시작되었다면 변경 불가, 종료 날짜", example = "localdatetime 값")
    })
    public ResponseEntity<AuctionEntity> update(@RequestHeader("Authorization") String token, @RequestBody AuctionModel auctionModel) {
        return ResponseEntity.ok(auctionService.update(token, auctionModel));
    }

    @DeleteMapping
    @Operation(summary = "상품 삭제", description = "상품 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameter(name = "id", description = "삭제할 상품의 id", example = "1")
    public ResponseEntity<String> delete(@RequestHeader("Authorization") String token, @RequestParam Long id) {
        return ResponseEntity.ok(auctionService.deleteById(token, id));
    }
}
