package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.UserModel;
import shop.biday.model.entity.UserEntity;
import shop.biday.service.UserService;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "users", description = "User Controller")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "유저 회원가입", description = "유저 회원가입할 때 사용하는 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "2002", description = "이미 가입된 계정입니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4000", description = "데이터베이스 연결에 실패하였습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "4011", description = "비밀번호 암호화에 실패하였습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameters({
            @Parameter(name = "email", description = "이메일", example = "chrome123@naver.com"),
            @Parameter(name = "password", description = "8자~12자 이내", example = "abcd1234"),
            @Parameter(name = "name", description = "이름", example = "코리아 시스템"),
            @Parameter(name = "phoneNum", description = "번호", example = "112233")
    })
    public String join(@RequestBody UserModel model) {
        userService.save(model);
        return "success!!";
    }

    @GetMapping("/")
    public List<UserEntity> findAll() {
        return userService.findAll();
    }

    @GetMapping("/findById/{id}")
    public Optional<UserEntity> findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @GetMapping("/existsById/{id}")
    public boolean existsById(@PathVariable Long id) {
        return userService.existsById(id);
    }

    @GetMapping("/count")
    public long count() {
        return userService.count();
    }

    @GetMapping("/deleteById/{id}")
    public void deleteById(@PathVariable Long id) {
        userService.deleteById(id);
    }
}
