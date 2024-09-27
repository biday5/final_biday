package shop.biday.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import shop.biday.oauth2.jwt.JWTUtil;
import shop.biday.utils.RedisTemplateUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class JWTReissueServiceImpl {

    private static final long ACCESS_TOKEN_EXPIRY_MS = 600000L; // 10 minutes
    private static final long REFRESH_TOKEN_EXPIRY_MS = 86400000L; // 1 day
    private static final String ACCESS_TOKEN_TYPE = "access";
    private static final String REFRESH_TOKEN_TYPE = "refresh";

    private final JWTUtil jwtUtil;
    private final RedisTemplateUtils<String> redisTemplateUtils;

    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new IllegalArgumentException("리프레시 토큰이 없습니다.");
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            log.error("error message : {}", e.getMessage());
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다.");
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다.");
        }

        String email = jwtUtil.getEmail(refresh);
        String role = jwtUtil.getRole(refresh);
        String name = jwtUtil.getName(refresh);

        String newAccess = jwtUtil.createJwt(ACCESS_TOKEN_TYPE, email, role, name, ACCESS_TOKEN_EXPIRY_MS);
        String newRefresh = jwtUtil.createJwt(REFRESH_TOKEN_TYPE, email, role, name, REFRESH_TOKEN_EXPIRY_MS);

        deleteRefreshToken(email);
        addRefreshEntity(email, newRefresh, REFRESH_TOKEN_EXPIRY_MS);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", newAccess);
        tokens.put("refresh", newRefresh);

        return tokens;
    }

    private void addRefreshEntity(String email, String refresh, Long expiredMs) {
        redisTemplateUtils.save(email, refresh, expiredMs);
    }

    private void deleteRefreshToken(String email) {
        redisTemplateUtils.delete(email);
    }

}
