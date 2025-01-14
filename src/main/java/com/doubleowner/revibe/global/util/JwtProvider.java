package com.doubleowner.revibe.global.util;

import com.doubleowner.revibe.domain.user.entity.User;
import com.doubleowner.revibe.domain.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    /**
     * JWT 시크릿 키.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * 토큰 만료시간(밀리초).
     */
    @Getter
    @Value("${jwt.access.expiry-millis}")
    private Long accessTokenExpiryMillis;

    @Getter
    @Value("${jwt.refresh.expiry-millis}")
    private Long refreshTokenExpiryMillis;

    private final UserRepository userRepository;

    public Map<String, String> generateTokens(Long userId) throws EntityNotFoundException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 존재하지 않습니다."));

        Map<String, String> tokens = new HashMap<>();

        Date currentDate = new Date();

        // Access token 생성
        Date accessTokenexpireDate = new Date(currentDate.getTime() + accessTokenExpiryMillis);

        String accessToken = Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(currentDate)
                .expiration(accessTokenexpireDate)
                .claim("role", user.getRole().name())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();

        // Refresh token 생성
        Date refreshTokenexpireDate = new Date(currentDate.getTime() + refreshTokenExpiryMillis);

        String refreshToken = Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(currentDate)
                .expiration(refreshTokenexpireDate)
                .claim("role", user.getRole().name())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
                .compact();

        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    public String getUsername(String token) {
        Claims claims = this.getClaims(token);
        return claims.getSubject();
    }

    public boolean validToken(String token) throws JwtException {
        try {
            return !this.tokenExpired(token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        }
        return false;
    }

    private Claims getClaims(String token) {
        if (!StringUtils.hasText(token)) {
            throw new MalformedJwtException("토큰이 비어 있습니다.");
        }

        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean tokenExpired(String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return this.resolveClaims(token, Claims::getExpiration);
    }

    private <T> T resolveClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.getClaims(token);
        return claimsResolver.apply(claims);
    }

}