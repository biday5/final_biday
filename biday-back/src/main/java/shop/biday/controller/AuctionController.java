package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.AuctionService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auctions")
@Tag(name = "auctions", description = "Auction Controller")
public class AuctionController {
    private final AuctionService auctionService;
}
