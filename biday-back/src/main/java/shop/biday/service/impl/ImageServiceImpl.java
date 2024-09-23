package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.biday.model.document.ImageDocument;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.repository.ImageRepository;
import shop.biday.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads";

    @Override
    public String save(MultipartFile[] files, String type, Long referencedId) {
        log.info("Image save start.");
        if (files.length == 0) {
            log.error("Image File is Empty.");
            return "파일이 비어 있습니다.";
        }

        StringBuilder resultMessage = new StringBuilder();

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                resultMessage.append("파일이 비어 있습니다.\n");
                continue;
            }

            String fileName = file.getOriginalFilename();
            String ext = getFileExtension(fileName);
            String baseFileName = getBaseFileName(fileName);

            Path localFilePath = Paths.get(UPLOAD_DIR, baseFileName);

            // Save file
            log.debug("Image file: {} baseFileName: {}, localFilePath: {}", fileName + ext, baseFileName, localFilePath);
            if (!saveFile(file, localFilePath, resultMessage)) continue;

            // Save image document
            ImageDocument savedImage = imageRepository.save(createImageDocument(baseFileName, ext, type, referencedId));
            log.debug("savedImage: {}", savedImage);
            resultMessage.append("파일 업로드 성공: ").append(baseFileName).append("\n");
        }
        return resultMessage.toString();
    }

    @Override
    public String update(MultipartFile file, String id) {
        log.info("Image update start.");
        if (file.isEmpty() || id == null) {
            log.error("File or Id is empty");
            return "파일 수정 실패: 파일 또는 ID가 비어 있습니다.";
        }

        Optional<ImageDocument> imageModelOpt = imageRepository.findById(id);
        if (!imageModelOpt.isPresent()) {
            log.error("Image not found for id: {}", id);
            return "파일 수정 실패: 해당 이미지를 찾을 수 없습니다.";
        }

        String fileName = file.getOriginalFilename();
        String ext = getFileExtension(fileName);
        String baseFileName = getBaseFileName(fileName);
        Path localFilePath = Paths.get(UPLOAD_DIR, baseFileName);

        // Save file
//        if (!saveFile(file, localFilePath, null)) {
//            return "파일 수정 실패: 저장 중 오류가 발생했습니다.";
//        }

        ImageDocument updatedImage = createImageDocument(baseFileName, ext, imageModelOpt.get().getType(), imageModelOpt.get().getReferencedId());
        updatedImage.setId(id);
        imageRepository.save(updatedImage);
        log.debug("Updated image: {}", updatedImage);

        return "파일 수정 성공: " + baseFileName;
    }

    private ImageDocument createImageDocument(String baseFileName, String ext, String type, Long referencedId) {
        return ImageDocument.builder()
                .name(baseFileName)
                .ext("." + ext)
                .url("C:\\Users\\kimdo\\Desktop\\final_biday\\biday-back\\src\\main\\resources\\static\\uploads\\" + baseFileName)
                .type(type)
                .referencedId(referencedId)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Override
    public String deleteById(String id) {
        log.info("Image delete start.");
        return Optional.of(id)
                .filter(imageRepository::existsById)
                .map(existingId -> {
                    imageRepository.deleteById(existingId);
                    return "success";
                })
                .orElse("fail");
    }

    @Override
    public ResponseEntity<byte[]> getImage(String id) {
        try {
            Optional<ImageDocument> image = imageRepository.findById(id);
            // 이미지 파일의 실제 경로 설정
            Path imagePath = Paths.get(image.get().getUrl() + image.get().getExt()); // 예: "images/"는 이미지가 저장된 디렉토리

            // 파일을 읽어옴
            byte[] imageBytes = Files.readAllBytes(imagePath);

            // MIME 타입을 추정
            String contentType = Files.probeContentType(imagePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(contentType != null ? contentType : "application/octet-stream")) // MIME 타입
                    .body(imageBytes); // 이미지 데이터
        } catch (IOException e) {
            log.error("Error reading image: {}", e.getMessage());
            return ResponseEntity.notFound().build(); // 파일을 찾을 수 없는 경우
        }
    }

    @Override
    public Optional<ImageDocument> findById(String id) {
        return imageRepository.findById(id);
    }

    @Override
    public ImageModel findByType(String type) {
        log.info("Find Image by Type: {}", type);
        return imageRepository.findByType(type);
    }

    @Override
    public ImageModel findByNameAndType(String name, String type) {
        log.info("Find Image by Name: {}, Type: {}", name, type);
        return imageRepository.findByNameAndType(name, type);
    }

    @Override
    public ImageModel findByNameAndTypeAndReferencedId(String name, String type, Long referencedId) {
        log.info("Find Image by Name: {} Type: {} ReferencedId: {}", name, type, referencedId);
        return imageRepository.findByNameAndTypeAndReferencedId(name, type, referencedId);
    }

    @Override
    public List<ImageModel> findAllByNameAndType(List<String> names, String type) {
        log.info("Find Image List by Name: {}, Type: {}", names, type);
        return imageRepository.findAllByNameAndType(names, type);
    }

    @Override
    public List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId) {
        log.info("Find Image List by Type: {}, ReferencedId: {}", type, referencedId);
        return imageRepository.findByTypeAndReferencedId(type, referencedId);
    }

    private boolean saveFile(MultipartFile file, Path path, StringBuilder resultMessage) {
        log.info("Save file started");
        try {
            file.transferTo(path);
            return true;
        } catch (IOException e) {
            if (resultMessage != null) {
                resultMessage.append("파일 저장 중 오류가 발생했습니다: ").append(e.getMessage()).append("\n");
            }
            log.error("파일 저장 중 오류가 발생했습니다: {}", e.getMessage());
            return false;
        }
    }

    private String getFileExtension(String fileName) {
        log.info("Get file extension: {}", fileName);
        return Optional.ofNullable(fileName)
                .map(name -> name.substring(name.lastIndexOf('.') + 1))
                .orElse("unknown");
    }

    private String getBaseFileName(String fileName) {
        log.info("Get base file name: {}", fileName);
        return Optional.ofNullable(fileName)
                .map(name -> name.substring(0, name.lastIndexOf('.')))
                .orElse("default");
    }

}
