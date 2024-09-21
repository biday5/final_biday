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
import shop.biday.model.domain.BrandModel;
import shop.biday.model.entity.BrandEntity;
import shop.biday.service.BrandService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
@Tag(name = "brands", description = "Brand Controller")
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    @Operation(summary = "브랜드 목록", description = "상품 등록하거나, 메인/검색 페이지에서 브랜드 목록 띄울 때 사용")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 목록 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 찾을 수 없음")
    })
    public ResponseEntity<List<BrandEntity>> findAll() {
        return ResponseEntity.ok(brandService.findAll());
    }

    @GetMapping("/findById")
    @Operation(summary = "브랜드 상세보기", description = "브랜드 상세보기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 찾을 수 없음")
    })
    @Parameter(name = "id", description = "상세보기할 브랜드 id", example = "1L")
    public ResponseEntity<Optional<BrandEntity>> findById(@RequestParam Long id) {
        return ResponseEntity.ok(brandService.findById(id));
    }

    @PostMapping
    @Operation(summary = "브랜드 등록", description = "브랜드 새로 등록하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 등록 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 등록 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "name", description = "브랜드 이름", example = "나이키(Nike)")
    })
    public ResponseEntity<BrandEntity> create(@RequestHeader("access") String token, @RequestBody BrandModel brand) {
        return ResponseEntity.ok(brandService.save(token, brand));
    }

    @PatchMapping
    @Operation(summary = "브랜드 수정", description = "브랜드 수정하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 수정 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "name", description = "브랜드 이름", example = "나이키(Nike)"),
            @Parameter(name = "createdAt", description = "등록 시간", example = "localDateTime 값"),
            @Parameter(name = "updatedAt", description = "수정 시간", example = "localDateTime 값")
    })
    public ResponseEntity<BrandEntity> update(@RequestHeader("access") String token, @RequestBody BrandModel brand) {
        return ResponseEntity.ok(brandService.update(token, brand));
    }

    @DeleteMapping
    @Operation(summary = "브랜드 삭제", description = "브랜드 삭제하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "브랜드 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "브랜드 삭제 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "id", description = "브랜드 id", example = "1L")
    })
    public void delete(@RequestHeader("access") String token, @RequestParam Long id) {
        brandService.deleteById(token, id);
    }
}
