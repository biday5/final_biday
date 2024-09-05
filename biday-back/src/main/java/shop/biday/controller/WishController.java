package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.WishService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
@Tag(name = "wishes", description = "Wish Controller")
public class WishController {
    private final WishService wishService;
}
