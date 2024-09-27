package shop.biday.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.biday.model.domain.UserModel;
import shop.biday.model.dto.UserDto;
import shop.biday.model.entity.UserEntity;
import shop.biday.service.UserService;
import shop.biday.service.impl.UserServiceImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "users", description = "User Controller")
public class UserController {
    private final UserServiceImpl userService;

    @PatchMapping("/passupdate")
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호가 성공적으로 변경되었습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "잘못된 요청. 올바르지 않은 비밀번호 형식이거나 기존 비밀번호가 맞지 않습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameter(description = "비밀번호 변경 요청", required = true)
    public ResponseEntity<String> changePassword(@RequestBody UserModel userModel) {
            return ResponseEntity.ok(userService.changePassword(userModel));
    }

    @PostMapping("/email")
    @Operation(summary = "전화번호로 이메일 조회", description = "제공된 전화번호에 연결된 이메일 주소를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일이 성공적으로 조회되었습니다.", content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "제공된 전화번호로 사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")
            )
    })
    @Parameter(name = "phone", description = "이메일을 조회할 전화번호", example = "123-456-7890"
    )
    public ResponseEntity<String> getEmailByPhone(@RequestBody UserModel userModel) {
            return ResponseEntity.ok(userService.getEmailByPhone(userModel));
    }

    @PostMapping("/password")
    @Operation(summary = "유저 비밀번호 검증", description = "소셜 로그인 후 이메일과 비밀번호 같은 검증 api")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "패스워드 확인", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "패스워드 검증이 실패 했습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "password", description = "검증할 패스워드 주소", example = "example@domain.com")
    public ResponseEntity<Boolean> checkPassword(@RequestBody UserModel userModel) {
        return new ResponseEntity<>(userService.existsByPasswordAndEmail(userModel), HttpStatus.OK);
    }

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
            @Parameter(name = "password", description = "8자~12자 이내", example = "abcd1234!@"),
            @Parameter(name = "name", description = "이름", example = "비트춘자"),
            @Parameter(name = "phoneNum", description = "번호", example = "000-0000-0000"),
            @Parameter(name = "streetaddress", description = "주소", example = "경기 성남시 중원구 성남동 2325"),
            @Parameter(name = "detailaddress", description = "상세주소", example = "102호"),
            @Parameter(name = "zipcode", description = "우편번호", example = "13363"),
            @Parameter(name = "type", description = "주소유형", example = "HOME")
    })
    public ResponseEntity<UserEntity> join(@RequestBody UserModel model) {
        return new ResponseEntity<>(userService.save(model), HttpStatus.OK);
    }

    @PostMapping("/validate")
    @Operation(summary = "유저 이메일 검증", description = "회원가입할 때 이메일이 이미 등록되어 있는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일이 사용 가능합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이메일이 이미 등록되어 있습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "email", description = "검증할 이메일 주소", example = "example@domain.com")
    public ResponseEntity<Boolean> validate(@RequestBody UserModel userModel) {
        return new ResponseEntity<>(userService.checkEmail(userModel), HttpStatus.OK);
    }

    @PostMapping("/phoneNum")
    @Operation(summary = "유저 핸드폰 검증", description = "회원가입할 때 핸드폰이 이미 등록되어 있는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "핸드폰번호가 사용 가능합니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "핸드폰번호가 이미 등록되어 있습니다.", content = @Content(mediaType = "application/json"))
    })
    @Parameter(name = "phoneNum", description = "번호", example = "000-0000-0000")
    public ResponseEntity<Boolean> phoneNum(@RequestBody UserModel userModel) {
        return new ResponseEntity<>(userService.checkPhone(userModel), HttpStatus.OK);
    }

    @GetMapping("/")
    public List<UserEntity> findAll() {
        return userService.findAll();
    }

    @GetMapping("/findById/{id}")
    @Operation(summary = "아이디로 이메일 조회", description = "제공된 아이디에 연결된 유저 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저가 성공적으로 조회되었습니다.", content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(responseCode = "404", description = "제공된 아이디로 사용자를 찾을 수 없습니다.", content = @Content(mediaType = "application/json")
            )
    })
    @Parameter(name = "id", description = "유저 조회할 아이디", example = "66f1442a7415bc47b04b3477"
    )
    public UserDto findById(@PathVariable String id) {
        return userService.findByUserId(id);
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
