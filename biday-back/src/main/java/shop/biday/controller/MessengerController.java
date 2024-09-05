package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.MessengerService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messengers")
@Tag(name = "messengers", description = "Messenger Controller")
public class MessengerController {
    private final MessengerService messengerService;
}
