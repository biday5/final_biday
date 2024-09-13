package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.ProductModel;
import shop.biday.model.dto.ProductDto;
import shop.biday.model.entity.ProductEntity;
import shop.biday.service.ProductService;

import java.util.List;

@CrossOrigin
@RestController
//@Controller
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "products", description = "Product Controller")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    @GetMapping("/findByFilter")
    @Operation(summary = "상품 목록", description = "메인에서 보여지거나, 검색 조건에 따른 상품 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 목록 가져오기 성공"),
            @ApiResponse( responseCode = "404", description = "상품 목록 찾을 수 없음")
    })
    @Parameters({
            @Parameter(name = "brandId", description = "브랜드 id", example = "1L"),
            @Parameter(name = "categoryId", description = "카테고리 id", example = "1L"),
            @Parameter(name = "keyword", description = "상품 키워드", example = "바지"),
            @Parameter(name = "color", description = "검색 이후 선택 가능한 색깔 필터", example = "red"),
            @Parameter(name = "order", description = "상품 목록 정렬 방식", example = "최신 등록순"),
            @Parameter(name = "lastItemValue", description = "현재 페이지에서 가장 마지막 상품의 id", example = "1L")
    })
    public ResponseEntity<Slice<ProductDto>> searchByFilter(
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "color", required = false) String color,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "lastItemValue", required = false) Long lastItemId, // 커서 값 추가
            Pageable pageable) {
        log.info("searchByFilter");
        log.debug("brandId: {} categoryId: {} keyword: {} color: {} order: {} lastItemId: {} page: {}", brandId, categoryId, keyword, color, order, lastItemId, pageable);
        return ResponseEntity.ok(productService.findByFilter(pageable, categoryId, brandId, keyword, color, order, lastItemId));
    }

    @GetMapping
    @Operation(summary = "상품 상세보기", description = "상품 리스트 혹은 마이페이지-찜 등에서 눌렀을 때 이동되는 상품 정보")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 가져오기 성공"),
            @ApiResponse( responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameter(name = "id", description = "선택된 상품의 id", example = "1L")
    public ResponseEntity<ProductModel> findById(@RequestParam(value = "id", required = true) Long id) {
        log.debug("Request to get product : {}", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    @Operation(summary = "상품 등록", description = "새로운 상품 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 등록 성공"),
            @ApiResponse( responseCode = "404", description = "상품 등록 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "brand", description = "브랜드 이름", example = "나이키"),
            @Parameter(name = "category", description = "카테고리 이름", example = "상의(Top)"),
            @Parameter(name = "name", description = "상품명", example = "UF 스타 로고 후디"),
            @Parameter(name = "subName", description = "상품명2", example = "UF star logo hoodie"),
            @Parameter(name = "productCode", description = "제품번호", example = "U23-TOP38-BR"),
            @Parameter(name = "price", description = "가격", example = "180000L"),
            @Parameter(name = "color", description = "색깔", example = "black"),
            @Parameter(name = "description", description = "상품 설명", example = "대비되는 컬러로 포인트를 줌과 동시에 나염+자수 기법 혼합하여 디자인하였습니다. 원단 및 시보리의 각 색상에 따라 염색 과정이 상이하여 색상 별로 중량, 텐션에 차이가 있을 수 있습니다.")
    })
    public ResponseEntity<ProductEntity> saveProduct(@RequestBody ProductModel product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @PatchMapping
    @Operation(summary = "상품 수정", description = "기존 상품 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @ApiResponse( responseCode = "404", description = "상품 수정 할 수 없음")
    })
    @Parameters({
            @Parameter(name = "brand", description = "브랜드 이름", example = "나이키"),
            @Parameter(name = "category", description = "카테고리 이름", example = "상의(Top)"),
            @Parameter(name = "name", description = "상품명", example = "UF 스타 로고 후디"),
            @Parameter(name = "subName", description = "상품명2", example = "UF star logo hoodie"),
            @Parameter(name = "productCode", description = "제품번호", example = "U23-TOP38-BR"),
            @Parameter(name = "price", description = "가격", example = "180000L"),
            @Parameter(name = "color", description = "색깔", example = "black"),
            @Parameter(name = "description", description = "상품 설명", example = "대비되는 컬러로 포인트를 줌과 동시에 나염+자수 기법 혼합하여 디자인하였습니다. 원단 및 시보리의 각 색상에 따라 염색 과정이 상이하여 색상 별로 중량, 텐션에 차이가 있을 수 있습니다.")
    })
    public ResponseEntity<ProductEntity> updateProduct(@RequestBody ProductModel product) {
        return ResponseEntity.ok(productService.update(product));
    }

    @DeleteMapping
    @Operation(summary = "상품 삭제", description = "상품 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            @ApiResponse( responseCode = "404", description = "상품 찾을 수 없음")
    })
    @Parameter(name = "id", description = "삭제할 상품의 id", example = "1L")
    public void deleteProduct(@RequestParam(value = "id", required = true) Long id) {
        productService.deleteById(id);
    }
}
