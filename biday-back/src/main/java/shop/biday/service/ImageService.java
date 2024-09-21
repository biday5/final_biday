package shop.biday.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import shop.biday.model.document.ImageDocument;
import shop.biday.model.domain.ImageModel;

import java.util.List;
import java.util.Optional;

public interface ImageService {
    String save(MultipartFile[] files, String type, Long referencedId);
    String update(MultipartFile file, String id);
    String deleteById(String id);
    ResponseEntity<byte[]> getImage(String id);
    Optional<ImageDocument> findById(String id);
    ImageModel findByType(String type);
    ImageModel findByNameAndType(String name, String type);
    ImageModel findByNameAndTypeAndReferencedId(String type, String name, Long referencedId);
    List<ImageModel> findAllByNameAndType(List<String> names, String type);
    List<ImageModel> findByTypeAndReferencedId(String type, Long referencedId);
}
