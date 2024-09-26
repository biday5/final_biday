package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.RatingModel;
import shop.biday.model.entity.RatingEntity;
import shop.biday.service.RatingService;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
@Tag(name = "ratings", description = "Rating Controller")
public class RatingController {
    private final RatingService ratingService;

    // 판매자 마이페이지나 경매 페이지에서 보여질 판매자의 총점 반환하는 메서드 만들어야 함

    @GetMapping
    @Operation(summary = "리뷰 목록", description = "판매자에 대한 리뷰 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 불러오기 성공"),
            @ApiResponse( responseCode = "404", description = "리뷰 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "access", description = "{token}", example = "??"),
            @Parameter(name = "id", description = "리뷰 불러올 판매자 id", example = "3sdfe3??")
    })
    // TODO : TOKEN에서 id 뽑아서 처리 할 것
    public ResponseEntity<List<RatingEntity>> findBySeller(@RequestHeader("Authorization") String token, @RequestParam("id") String sellerId) {
        return ResponseEntity.ok(ratingService.findBySeller(token, sellerId));
    }

    @PostMapping
    @Operation(summary = "리뷰 등록", description = "새로운 리뷰 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 등록 성공"),
            @ApiResponse( responseCode = "404", description = "리뷰 등록 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "access", description = "{token}", example = "??"),
            @Parameter(name = "userId", description = "판매자 id", example = "1"),
            @Parameter(name = "rating", description = "판매자에 대한 평가", example = "3"),
    })
    public ResponseEntity<RatingEntity> save(@RequestHeader("Authorization") String token, @RequestBody RatingModel rating) {
        return ResponseEntity.ok(ratingService.save(token, rating));
    }
}
