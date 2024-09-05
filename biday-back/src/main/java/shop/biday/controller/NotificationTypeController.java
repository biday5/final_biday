package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.NotificationTypeService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notificationTypes")
@Tag(name = "notificationTypes", description = "NotificationType Controller")
public class NotificationTypeController {
    private final NotificationTypeService notificationTypeService;
}
