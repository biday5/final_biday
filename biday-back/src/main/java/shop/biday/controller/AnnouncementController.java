package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.AnnouncementService;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announcements")
@Tag(name = "announcements", description = "Announcement Controller")
public class AnnouncementController {
    private final AnnouncementService announcementService;
}
