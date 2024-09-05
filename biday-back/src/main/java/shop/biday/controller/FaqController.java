package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.FaqService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/faqs")
@Tag(name = "faqs", description = "FAQs Controller")
public class FaqController {
    private final FaqService faqService;
}
