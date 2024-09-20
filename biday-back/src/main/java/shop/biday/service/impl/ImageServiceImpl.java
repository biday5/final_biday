package shop.biday.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.biday.model.document.ImageDocument;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.repository.ImageRepository;
import shop.biday.service.ImageService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private static final String UPLOAD_DIR = "C:\\Users\\kimdo\\Desktop\\biday_data\\uploads\\";

    @Override
    public String save(MultipartFile[] files, String type, Long referencedId) {
        if (files.length == 0) {
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
        if (!saveFile(file, localFilePath, null)) {
            return "파일 수정 실패: 저장 중 오류가 발생했습니다.";
        }

        ImageDocument updatedImage = createImageDocument(baseFileName, ext, imageModelOpt.get().getType(), imageModelOpt.get().getReferencedId());
        updatedImage.setId(id);
        imageRepository.save(updatedImage);
        log.debug("Updated image: {}", updatedImage);

        return "파일 수정 성공: " + baseFileName;
    }

    @Override
    public String deleteById(String id) {
        try {
            if (imageRepository.existsById(id)) {
                imageRepository.deleteById(id);
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            log.error("Error deleting image: {}", e.getMessage());
            return "fail";
        }
    }

    @Override
    public ImageModel findByType(String type) {
        return safeExecute(() -> imageRepository.findByType(type));
    }

    @Override
    public ImageModel findByNameAndType(String name, String type) {
        return safeExecute(() -> imageRepository.findByNameAndType(name, type));
    }

    @Override
    public List<ImageModel> findAllByNameAndType(List<String> names, String type) {
        return safeExecute(() -> imageRepository.findAllByNameAndType(names, type));
    }

    @Override
    public List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId) {
        return safeExecute(() -> imageRepository.findByTypeAndReferencedId(type, referencedId));
    }

    private boolean saveFile(MultipartFile file, Path path, StringBuilder resultMessage) {
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

    private ImageDocument createImageDocument(String baseFileName, String ext, String type, Long referencedId) {
        return ImageDocument.builder()
                .name(baseFileName)
                .ext("." + ext)
                .url("http://localhost:8080/uploads/" + baseFileName)
                .type(type)
                .referencedId(referencedId)
                .build();
    }

    private String getFileExtension(String fileName) {
        return fileName != null ? fileName.substring(fileName.lastIndexOf('.') + 1) : "unknown";
    }

    private String getBaseFileName(String fileName) {
        return fileName != null ? fileName.substring(0, fileName.lastIndexOf('.')) : "default";
    }

    private <T> T safeExecute(SafeOperation<T> operation) {
        try {
            return operation.execute();
        } catch (Exception e) {
            log.error("Error executing operation: {}", e.getMessage());
            return null;
        }
    }

    @FunctionalInterface
    private interface SafeOperation<T> {
        T execute() throws Exception;
    }
}
