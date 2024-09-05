package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.RatingService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ratings")
@Tag(name = "ratings", description = "Rating Controller")
public class RatingController {
    private final RatingService ratingService;
}
