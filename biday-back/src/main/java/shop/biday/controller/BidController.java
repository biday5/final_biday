package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.BidService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
@Tag(name = "bids", description = "Bid Controller")
public class BidController {
    private final BidService bidService;
}
