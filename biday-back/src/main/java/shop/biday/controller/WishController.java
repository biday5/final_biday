package shop.biday.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.repository.WishRepository;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.service.WishService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishes")
@Tag(name = "wishes", description = "Wish Controller")
public class WishController {
    private final WishService wishService;
    private final WishRepository wishRepository;
    private final JWTUtil jwtUtil;


    @GetMapping("/user")
    public ResponseEntity<List<?>> findByUser(@RequestHeader("Authrozation") String token) {

        String email = jwtUtil.getEmail(token);

        List<?> wishList = wishRepository.findByUserEmail(email);

        return (wishList == null || wishList.isEmpty())
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(wishList);

    }

    @GetMapping
    public ResponseEntity<?> toggleWish(@RequestHeader("Authorization") String token, @RequestParam Long productId) {

        String email = jwtUtil.getEmail(token);

        return wishService.toggleWIsh(email, productId)
                ? ResponseEntity.status(HttpStatus.CREATED).body("위시 생성 성공")
                : ResponseEntity.ok("위시 삭제 성공");

    }

    @DeleteMapping
    public ResponseEntity<?> delete(@RequestHeader("Authorization") String token, @RequestParam Long id) {

        return Optional.of(id)
                .map(wishId -> {
                    wishRepository.deleteById(wishId);
                    return ResponseEntity.ok("위시 삭제 성공");
                }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 wishId: " + id));

    }
}