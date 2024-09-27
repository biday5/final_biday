package shop.biday.service.impl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import shop.biday.model.document.ImageDocument;
import shop.biday.model.domain.ImageModel;
import shop.biday.model.repository.ImageRepository;
import shop.biday.model.repository.UserRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.ImageService;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    private final AmazonS3Client amazonS3Client;
    @Value("${spring.s3.bucket}")
    private String bucketName;

    @Override
    public String uploadFileByAdmin(String token, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        log.info("Image upload By Admin started");
        return validateAdmin(token)
                .map(validToken -> upload(multipartFiles, filePath, type, referencedId))
                .orElseThrow(() -> new IllegalArgumentException("User does not have the necessary permissions or the token is invalid."));
    }

    @Override
    public String uploadFilesByUser(String token, List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        log.info("Images upload By User started");
        return validateUser(token)
                .map(validToken -> upload(multipartFiles, filePath, type, referencedId))
                .orElseThrow(() -> new IllegalArgumentException("User does not have the necessary permissions or the token is invalid."));
    }

    private String upload(List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        if (multipartFiles.isEmpty()) {
            log.error("File list is empty");
            return "파일이 비어있습니다.";
        }

        StringBuilder resultMessage = new StringBuilder();
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                resultMessage.append("파일이 비어 있습니다.\n");
                return resultMessage.toString();
            }

            String originalFileName = multipartFile.getOriginalFilename();
            String fileExtension = getFileExtension(originalFileName);
            System.out.println(fileExtension);
            String uploadFileName = getUuidFileName(originalFileName);
            String uploadFileUrl = uploadToS3(multipartFile, filePath, uploadFileName, originalFileName, resultMessage);

            if (uploadFileUrl != null) {
                saveImageDocument(originalFileName, uploadFileName, filePath, uploadFileUrl, type, referencedId);
                resultMessage.append("파일 업로드 성공: ").append(originalFileName).append("\n");
            }
        }
        return resultMessage.toString();
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == fileName.length() - 1) {
            return ""; // No extension found
        }
        return fileName.substring(lastDotIndex + 1);
    }

    private String uploadToS3(MultipartFile file, String filePath, String uploadFileName, String originalFileName, StringBuilder resultMessage) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            String keyName = filePath + "/" + uploadFileName;

            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            return "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;

        } catch (IOException e) {
            log.error("Error uploading file: {}", e.getMessage());
            resultMessage.append("파일 업로드 실패: ").append(originalFileName).append("\n");
            return null;
        }
    }

    private void saveImageDocument(String originalFileName, String uploadFileName, String filePath, String uploadFileUrl, String type, Long referencedId) {
        log.info("Image save started");
        ImageDocument image = ImageDocument.builder()
                .originalName(originalFileName)
                .uploadName(uploadFileName)
                .uploadPath(filePath)
                .uploadUrl(uploadFileUrl)
                .type(type)
                .referencedId(referencedId)
                .createdAt(LocalDateTime.now())
                .build();
        imageRepository.save(image);
        log.debug("Image saved to Mongo: {}", imageRepository.findById(image.getId()));
    }

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID() + "." + ext;
    }

    @Override
//    public String update(String token, List<MultipartFile> multipartFiles, String id) {
    public String update(List<MultipartFile> multipartFiles, String id) {
        log.info("Image update started for ID: {}", id);
        Optional<ImageDocument> imageModel = imageRepository.findById(id);
        StringBuilder resultMessage = new StringBuilder();

        if (imageModel.isEmpty()) {
            log.error("Image not found: {}", id);
            return "이미지 찾을 수 없습니다.";
        }

        ImageDocument image = imageModel.get();
        if (multipartFiles.isEmpty()) {
            log.error("File is empty");
            return "업로드할 파일이 비어있습니다.";
        }

        for(MultipartFile file : multipartFiles) {
            String originalFileName = file.getOriginalFilename();
            String uploadFileName = getUuidFileName(originalFileName);
            String uploadFileUrl = uploadToS3(file, image.getUploadPath(), uploadFileName, originalFileName, resultMessage);

            if (uploadFileUrl != null) {
                image.setOriginalName(originalFileName);
                image.setUploadName(uploadFileName);
                image.setUploadUrl(uploadFileUrl);
                image.setUpdatedAt(LocalDateTime.now());

                imageRepository.save(image);
                log.debug("Image updated in Mongo: {}", imageRepository.findById(image.getId()));
                resultMessage.append("파일 업데이트 성공: ").append(originalFileName).append("\n");
            }
        }

        return resultMessage.toString();
    }

    @Override
    public ResponseEntity<byte[]> getImage(String id) {
        log.info("Get Image: {}", id);
        try {
            Optional<ImageDocument> image = imageRepository.findById(id);
            if (image.isEmpty()) {
                log.error("Image Name Not Found: {}", image.get().getOriginalName());
                ImageModel errorImage = imageRepository.findByTypeAndUploadPath("에러", "error");
                S3Object s3Object = amazonS3Client.getObject(bucketName, errorImage.getUploadPath() + "/" + errorImage.getUploadName());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(IOUtils.toByteArray(s3Object.getObjectContent()));
            } else {
                log.debug("Image Name: {}", image.get().getOriginalName());
                S3Object s3Object = amazonS3Client.getObject(bucketName, image.get().getUploadPath() + "/" + image.get().getUploadName());
                log.debug("Get Image By Storage: {}", s3Object.getBucketName() + "/" + s3Object.getKey());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(IOUtils.toByteArray(s3Object.getObjectContent()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteById(String id) {
        log.info("Image delete start.");
        return Optional.of(id)
                .filter(imageRepository::existsById)
                .map(existingId -> {
                    imageRepository.deleteById(existingId);
                    log.debug("Image deleted To Mongo: {}", existingId);
                    return "이미지 삭제 성공";
                })
                .orElse("이미지 삭제 실패");
    }

    @Override
    public Optional<ImageDocument> findById(String id) {
        log.info("Find Image By id: {}", id);
        return imageRepository.findById(id);
    }

    @Override
    public ImageModel findByTypeAndUploadPath(String type, String uploadPath) {
        log.info("Find Image by Type: {}", type);
        return imageRepository.findByType(type);
    }

    @Override
    public ImageModel findByOriginalNameAndType(String name, String type) {
        log.info("Find Image by Name: {}, Type: {}", name + ".jpg", type);
        return imageRepository.findByOriginalNameAndType(name + ".jpg", type);
    }

    @Override
    public ImageModel findByOriginalNameAndTypeAndReferencedId(String name, String type, Long referencedId) {
        log.info("Find Image by Name: {} Type: {} ReferencedId: {}", name + ".jpg", type, referencedId);
        return imageRepository.findByOriginalNameAndTypeAndReferencedId(name + ".jpg", type, referencedId);
    }

    @Override
    public ImageModel findByTypeAndReferencedIdAndUploadPath(String type, String referencedId, String uploadPath) {
        log.info("Find Image by Type: {} ReferencedId: {} UploadPath: {}", type, referencedId, uploadPath);
        return imageRepository.findByTypeAndReferencedIdAndUploadPath(type, referencedId, uploadPath);
    }

    @Override
    public List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId) {
        log.info("Find Image List by Type: {}, ReferencedId: {}", type, referencedId);
        return imageRepository.findByTypeAndReferencedId(type, referencedId);
    }

    private Optional<String> validateUser(String token) {
        log.info("Validate User started for token: {}", token);
        return Optional.of(token)
                .filter(t -> {
                    String role = jwtUtil.getRole(t);
                    return role.equalsIgnoreCase("ROLE_SELLER") || role.equalsIgnoreCase("ROLE_USER");
                })
                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
                .or(() -> {
                    log.error("User does not have role SELLER or USER or does not exist for token: {}", token);
                    return Optional.empty();
                });
    }

    private Optional<String> validateAdmin(String token) {
        log.info("Validate Admin started for token: {}", token);
        return Optional.of(token)
                .filter(t -> jwtUtil.getRole(t).equalsIgnoreCase("ROLE_ADMIN"))
                .filter(t -> userRepository.existsByEmail(jwtUtil.getEmail(t)))
                .or(() -> {
                    log.error("User does not have role SELLER or does not exist for token: {}", token);
                    return Optional.empty();
                });
    }
}
