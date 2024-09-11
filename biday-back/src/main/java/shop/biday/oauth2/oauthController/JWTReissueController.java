package shop.biday.oauth2.oauthController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import shop.biday.oauth2.UserDetailsService.JWTReissueService;

import java.util.Map;


@Controller
@ResponseBody
@RequiredArgsConstructor
public class JWTReissueController {

    private final JWTReissueService JWTReissueService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> tokens = JWTReissueService.refreshToken(request, response);

        System.out.println("tokens : "+tokens);

        response.setHeader("access", tokens.get("access"));
        response.addCookie(createCookie("refresh", tokens.get("refresh")));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
