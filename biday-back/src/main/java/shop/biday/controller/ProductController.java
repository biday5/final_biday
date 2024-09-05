package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.ProductService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@Tag(name = "products", description = "Product Controller")
public class ProductController {
    private final ProductService productService;
}
