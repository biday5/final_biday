package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.BrandService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brands")
@Tag(name = "brands", description = "Brand Controller")
public class BrandController {
    private final BrandService brandService;
}
