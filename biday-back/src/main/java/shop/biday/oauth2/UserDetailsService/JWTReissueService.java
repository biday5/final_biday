package shop.biday.oauth2.UserDetailsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import shop.biday.oauth2.jwt.JWTUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JWTReissueService {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) {

        // 새로 고침 토큰 가져오기
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new IllegalArgumentException("Refresh token is null");
        }

        // 새로 고침 토큰이 만료되었는지 확인합니다.
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Refresh token expired");
        }

        // 토큰 범주 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        // Generate new tokens
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        //Refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        deleteRefreshToken(username);
        addRefreshEntity(username, newRefresh, 86400000L);

        // Return tokens in a map
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access", newAccess);
        tokens.put("refresh", newRefresh);

        return tokens;
    }

    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        System.out.println("redis insert : "+ refresh);
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(username, refresh, expiredMs, TimeUnit.MILLISECONDS);
    }
    private void deleteRefreshToken(String username) {
        System.out.println("redis delete username : "+ username);
        // Redis에서 토큰 삭제
        redisTemplate.delete(username);
    }

}
