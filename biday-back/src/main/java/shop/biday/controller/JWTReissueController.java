package shop.biday.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.biday.service.impl.JWTReissueServiceImpl;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class JWTReissueController {

    private final JWTReissueServiceImpl JWTReissueServiceImpl;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> tokens = JWTReissueServiceImpl.refreshToken(request, response);

        response.setHeader("access", tokens.get("access"));
        response.addCookie(createCookie("refresh", tokens.get("refresh")));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24 * 60 * 60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
