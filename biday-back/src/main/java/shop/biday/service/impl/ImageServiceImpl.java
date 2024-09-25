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

    private final AmazonS3Client amazonS3Client;
    @Value("${spring.s3.bucket}")
    private String bucketName;

    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.indexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }

    @Override // 수정 해야함
    public String uploadFiles(List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId) {
        log.info("Image upload started");
        if (multipartFiles.isEmpty()) {
            log.error("File is empty");
            return "파일이 비어있습니다.";
        }

        StringBuilder resultMessage = new StringBuilder();

//        String filePath = switch (type){
//            // type 따라서? 어케 할지 모르겟삼
//        }

        List<ImageModel> s3files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) {
                resultMessage.append("파일이 비어 있습니다.\n");
                continue;
            }

            String originalFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getUuidFileName(originalFileName);
            String uploadFileUrl = "";

            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());

            try (InputStream inputStream = multipartFile.getInputStream()) {

                String keyName = filePath + "/" + uploadFileName;

                amazonS3Client.putObject(
                        new PutObjectRequest(bucketName, keyName, inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));

                uploadFileUrl = "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;

            } catch (IOException e) {
                e.printStackTrace();
            }

            ImageModel imageModel = ImageModel.builder()
                    .originalName(originalFileName)
                    .uploadName(uploadFileName)
                    .uploadPath(filePath)
                    .uploadUrl(uploadFileUrl)
                    .type(type)
                    .referencedId(referencedId)
                    .createdAt(LocalDateTime.now())
                    .build();

            ImageDocument image = imageRepository.save(createImageDocument(originalFileName, uploadFileName, filePath, uploadFileUrl, type, referencedId));
            log.debug("Image saved To Mongo: {}", imageRepository.findById(image.getId()));
            s3files.add(imageModel);
            log.debug("Image saved To Storage: {}", image.getOriginalName());
            resultMessage.append("파일 업로드 성공: ").append(originalFileName).append("\n");
        }

        return resultMessage.toString();
    }

    private ImageDocument createImageDocument(String originalFileName, String uploadName, String uploadPath, String uploadUrl, String type, Long referencedId) {
        log.info("Creating image document: {}", originalFileName);
        return ImageDocument.builder()
                .originalName(originalFileName)
                .uploadName(uploadName)
                .uploadPath(uploadPath)
                .uploadUrl(uploadUrl)
                .type(type)
                .referencedId(referencedId)
                .createdAt(LocalDateTime.now())
                .build();
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
    public String update(MultipartFile file, String id) {
        return "";
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
    public Optional<ImageDocument> findById(String id) {
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
    public List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId) {
        log.info("Find Image List by Type: {}, ReferencedId: {}", type, referencedId);
        return imageRepository.findByTypeAndReferencedId(type, referencedId);
    }
}
