package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.ChatClickService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatClicks")
@Tag(name = "chatClicks", description = "ChatClick Controller")
public class ChatClickController {
    private final ChatClickService chatClickService;
}
