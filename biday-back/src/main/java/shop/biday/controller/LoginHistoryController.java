package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.LoginHistoryService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loginHistories")
@Tag(name = "loginHistories", description = "LoginHistory Controller")
public class LoginHistoryController {
    private final LoginHistoryService loginHistoryService;
}
