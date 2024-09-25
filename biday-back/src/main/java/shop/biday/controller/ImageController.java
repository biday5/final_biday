package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import shop.biday.service.ImageService;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "images", description = "Image Controller")
public class ImageController {
    private final ImageService imageService;

    @GetMapping("/findImage")
    @Operation(summary = "이미지 불러오기", description = "이미지 불러오기.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 불러오기 성공"),
            @ApiResponse( responseCode = "404", description = "사진 불러오기 실패")
    })
    @Parameters(value = {
            @Parameter(description = "이미지의 ID")
    })
    public ResponseEntity<byte[]> getImageById(@RequestParam("id") String id) {
        log.info("이미지 불러오는 중");
        return imageService.getImage(id);
    }

    @PostMapping("/upload")
    @Operation(summary = "이미지 업로드", description = "여러 이미지를 업로드합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 등록 성공"),
            @ApiResponse( responseCode = "404", description = "사진 등록 실패")
    })
    @Parameters(value = {
            @Parameter(description = "업로드할 이미지 파일 목록"),
            @Parameter(description = "이미지 타입"),
            @Parameter(description = "이미지의 참조 ID")
    })
    public String uploadImages(
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("filePath") String filePath,
            @RequestParam("type") String type,
            @RequestParam("referenceId") Long referenceId
    ) {
        log.info("이미지 업로드 중");
        return imageService.uploadFiles(files, filePath, type, referenceId).toString();
    }

    @PatchMapping
    @Operation(summary = "이미지 업데이트", description = "ID로 기존 이미지를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 수정 성공"),
            @ApiResponse( responseCode = "404", description = "사진 수정 성공")
    })
    @Parameters(value = {
            @Parameter(description = "업데이트할 이미지 파일"),
            @Parameter(description = "업데이트할 이미지의 ID")
    })
    public String updateImages(
            @RequestParam("files") MultipartFile file,
            @RequestParam("id") String id) {
        log.info("이미지 업데이트 중");
        return imageService.update(file, id);
    }

    @DeleteMapping
    @Operation(summary = "이미지 삭제", description = "ID로 이미지를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사진 삭제 성공"),
            @ApiResponse( responseCode = "404", description = "사진 삭제 실패")
    })
    @Parameter(description = "삭제할 이미지의 ID")
    public String deleteImages(
            @RequestParam("id") String id) {
        log.info("이미지 삭제 중");
        return imageService.deleteById(id);
    }

}
