package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.ImageService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
@Tag(name = "images", description = "Image Controller")
public class ImageController {
    private final ImageService imageService;
}
