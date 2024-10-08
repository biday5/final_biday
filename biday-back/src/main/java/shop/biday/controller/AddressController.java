package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.AddressModel;
import shop.biday.model.entity.AddressEntity;
import shop.biday.service.impl.AddressServiceImpl;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
@Tag(name = "addresses", description = "Address Controller")
public class AddressController {
    private final AddressServiceImpl addressService;

    @GetMapping("/list")
    @Operation(summary = "주소 목록", description = "상품 등록이나 메인/검색 페이지에서 카테고리 목록을 불러올 때 사용합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소 목록을 성공적으로 불러왔습니다."),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.")
    })
    @Parameter(name = "access", description = "{token}", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    public ResponseEntity<List<AddressEntity>> findAll(@RequestHeader("access") String token) {
        return ResponseEntity.ok(addressService.findAllByEmail(token));
    }

    @PutMapping("/pick")
    @Operation(
            summary = "주소 선택",
            description = "주어진 ID의 주소를 '선택됨' 상태로 업데이트하고, 동일한 사용자에 대해 다른 주소들은 '선택되지 않음'으로 업데이트합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소가 성공적으로 업데이트되었습니다."),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.", content = @Content)
    })
    @Parameter(name = "id", description = "기본주소로 선택할 id", example = "1L")
    public ResponseEntity<String> pick(@RequestParam @Parameter(description = "선택할 주소의 ID") Long id) {
        return ResponseEntity.ok(addressService.pick(id));
    }

    @GetMapping("/count")
    @Operation(
            summary = "주소 수 카운트",
            description = "주어진 이메일과 연관된 주소의 수를 카운트합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일과 연관된 주소의 수."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.", content = @Content)
    })
    @Parameter(name = "email", description = "주소 리스트의 이메일", example = "example@naver.com")
    public ResponseEntity<Long> count(@RequestHeader("access") String token) {
        return ResponseEntity.ok(addressService.countByEmail(token));
    }

    @DeleteMapping("/deleteById")
    @Operation(
            summary = "주소 삭제",
            description = "주어진 ID의 주소를 삭제합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "주소가 성공적으로 삭제되었습니다."),
            @ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.", content = @Content)
    })
    @Parameter(name = "id", description = "삭제 할 주소를 선택 id", example = "1L")
    public ResponseEntity<Void> deleteById(@RequestParam @Parameter(description = "삭제할 주소의 ID") Long id) {
        try {
            addressService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/insert")
    @Operation(
            summary = "주소 등록",
            description = "사용자를 위한 새 주소를 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주소가 성공적으로 등록되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 입력입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 오류가 발생했습니다.", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<AddressEntity> insert(
            @RequestBody @Parameter(description = "주소 세부 사항을 포함하는 모델") AddressModel addressModel) {
        log.info("addressModel : {}", addressModel);
        return ResponseEntity.ok(addressService.save(addressModel));
    }

}
