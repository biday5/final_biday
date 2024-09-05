package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.ChatButtonService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatButtons")
@Tag(name = "chatButtons", description = "ChatButton Controller")
public class ChatButtonController {
    private final ChatButtonService chatButtonService;
}
