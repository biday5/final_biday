package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.AddressService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
@Tag(name = "addresses", description = "Address Controller")
public class AddressController {
    private final AddressService addressService;
}
