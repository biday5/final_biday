package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.repository.WishRepository;
import shop.biday.service.WishService;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
@Tag(name = "wishes", description = "Wish Controller")
public class WishController {
    private final WishService wishService;
    private final WishRepository wishRepository;


    @GetMapping("/{id}")
    public ResponseEntity<List<?>> findByUser(@PathVariable Long id) {

        List<?> wishList = wishRepository.findByUserId(id);
        System.out.println("wishList = " + wishList);

        if (wishList == null || wishList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(wishList);
    }

    @GetMapping
    public ResponseEntity<?> toggleWish(@RequestParam Long userId, @RequestParam Long productId) {

        boolean result = wishService.toggleWIsh(userId, productId);

        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("위시 생성 성공");
        } else {
            return ResponseEntity.ok("위시 삭제 성공");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        System.out.println("deleteMapping");

        try {
            wishRepository.deleteById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 wishId: " + id);
        }

        return ResponseEntity.ok("위시 삭제 성공");
    }
}
