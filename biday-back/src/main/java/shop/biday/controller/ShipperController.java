package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.ShipperService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shippers")
@Tag(name = "shippers", description = "Shipper Controller")
public class ShipperController {
    private final ShipperService shipperService;
}
