package shop.biday.oauth2.jwt;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.model.entity.LoginHistoryEntity;
import shop.biday.oauth2.OauthDto.CustomOAuth2User;
import shop.biday.service.impl.LoginHistoryServiceImpl;
import shop.biday.utils.RedisTemplateUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final long ACCESS_TOKEN_EXPIRY_MS = 600000L; // 10 minutes
    private static final long REFRESH_TOKEN_EXPIRY_MS = 86400000L; // 1 day
    private static final int COOKIE_MAX_AGE_SECONDS = 24 * 60 * 60; // 1 day
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";
    private static final String REDIRECT_URL = "http://localhost:3000/";
    private static final int HTTP_OK_STATUS = HttpStatus.OK.value();

    private final JWTUtil jwtUtil;
    private final LoginHistoryServiceImpl loginHistoryService;
    private final RedisTemplateUtils<String > redisTemplateUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        String email = customUserDetails.getEmail();
        String name  = customUserDetails.getName();
        Long   id    = customUserDetails.getId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt(ACCESS_TOKEN_TYPE, email, role, name,  ACCESS_TOKEN_EXPIRY_MS);
        String refresh = jwtUtil.createJwt(REFRESH_TOKEN_TYPE, email, role, name,  REFRESH_TOKEN_EXPIRY_MS);

        addRefreshEntity(email, refresh, REFRESH_TOKEN_EXPIRY_MS);

        if(loginHistoryService.findByUserId(id).isEmpty()) {
            LoginHistoryModel loginHistoryModel = new LoginHistoryModel();
            loginHistoryModel.setUserId(id);
            loginHistoryService.save(loginHistoryModel);
        }

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HTTP_OK_STATUS);
        response.sendRedirect(REDIRECT_URL);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        // https 통신 진행시 주석 해제
        // cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {
        redisTemplateUtils.save(email,refresh,expiredMs);
    }
}
