package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.NotificationService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "notifications", description = "Notification Controller")
public class NotificationController {
    private final NotificationService notificationService;
}
