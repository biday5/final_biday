package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.CategoryService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
@Tag(name = "categories", description = "Category Controller")
public class CategoryController {
    private final CategoryService categoryService;
}
