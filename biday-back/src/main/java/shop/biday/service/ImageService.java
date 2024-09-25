package shop.biday.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import shop.biday.model.document.ImageDocument;
import shop.biday.model.domain.ImageModel;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    String uploadFiles(List<MultipartFile> multipartFiles, String filePath, String type, Long referencedId);
    String update(MultipartFile file, String id);
    String deleteById(String id);
    ResponseEntity<byte[]> getImage(String id);
    Optional<ImageDocument> findById(String id);
    ImageModel findByTypeAndUploadPath(String type, String uploadPath);
    ImageModel findByOriginalNameAndType(String name, String type);
    ImageModel findByOriginalNameAndTypeAndReferencedId(String type, String name, Long referencedId);
    List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId);
}
