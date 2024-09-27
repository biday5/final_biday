package shop.biday.oauth2.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shop.biday.model.domain.LoginHistoryModel;
import shop.biday.oauth2.UserDetailsService.CustomUserDetails;
import shop.biday.service.impl.LoginHistoryServiceImpl;
import shop.biday.utils.RedisTemplateUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private static final long ACCESS_TOKEN_EXPIRY_MS = 600000L; // 10 minutes
    private static final long REFRESH_TOKEN_EXPIRY_MS = 86400000L; // 1 day
    private static final int COOKIE_MAX_AGE_SECONDS = 24 * 60 * 60; // 1 day
    private static final int HTTP_UNAUTHORIZED_STATUS = 401;
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisTemplateUtils<String> redisTemplateUtils;
    private final LoginHistoryServiceImpl loginHistoryService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String email = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = customUserDetails.getEmail();
        String name = customUserDetails.getName();
        Long id = customUserDetails.getId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String access = jwtUtil.createJwt(ACCESS_TOKEN_TYPE, email, role, name, ACCESS_TOKEN_EXPIRY_MS);
        String refresh = jwtUtil.createJwt(REFRESH_TOKEN_TYPE, email, role, name, REFRESH_TOKEN_EXPIRY_MS);

        addRefreshEntity(email, refresh, REFRESH_TOKEN_EXPIRY_MS);

        if (loginHistoryService.findByUserId(id).isEmpty()) {
            LoginHistoryModel loginHistoryModel = new LoginHistoryModel();
            loginHistoryModel.setUserId(id);
            loginHistoryService.save(loginHistoryModel);
        }

        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

        //redirect 3000번으로 전달
        //response.sendRedirect("http://localhost:3000");
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HTTP_UNAUTHORIZED_STATUS);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        //https 통신 진행시 주석 해제
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {
        redisTemplateUtils.save(email, refresh, expiredMs);
    }
}

