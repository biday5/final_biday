package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    @Operation(summary = "리뷰 목록", description = "판매자에 대한 리뷰 찾기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 불러오기 성공"),
            @ApiResponse( responseCode = "404", description = "리뷰 찾을 수 없음")
    })
    @Parameter(name = "id", description = "리뷰 불러올 판매자 id", example = "1L")
    public List<RatingEntity> findBySeller(Long sellerId) {
        return ratingService.findBySeller(sellerId);
    }

    @PostMapping
    @Operation(summary = "리뷰 등록", description = "새로운 리뷰 작성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 등록 성공"),
            @ApiResponse( responseCode = "404", description = "리뷰 등록 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "userId", description = "판매자 id", example = "1L"),
            @Parameter(name = "rating", description = "판매자에 대한 평가", example = "3"),
    })
    public RatingEntity save(@RequestBody RatingModel rating) {
        return ratingService.save(rating);
    }
}
