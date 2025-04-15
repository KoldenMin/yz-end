package com.example.yz1.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private RedisUtil redisUtil;

    // Redis中存储token的key前缀
    private static final String TOKEN_KEY_PREFIX = "jwt:token:";

    /**
     * 生成token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param isAdmin  是否管理员
     * @return token
     */
    public String generateToken(Long userId, String username, boolean isAdmin) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("isAdmin", isAdmin);

        String token = createToken(claims);
        String redisKey = TOKEN_KEY_PREFIX + userId;
        redisUtil.set(redisKey, token, expiration);
        return token;
    }

    /**
     * 从redis获取token
     *
     * @param userId 用户id
     * @return token
     */

    public String getTokenFromRedis(Long userId) {
        String redisKey = TOKEN_KEY_PREFIX + userId;
        Object token = redisUtil.get(redisKey);
        return token != null ? token.toString() : null;
    }

    /**
     * 使token失效
     *
     * @param userId 用户ID
     */
    public void invalidateToken(Long userId) {
        String redisKey = TOKEN_KEY_PREFIX + userId;
        redisUtil.delete(redisKey);
    }

    /**
     * 从token中获取用户ID
     *
     * @param token token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 从token中获取用户名
     *
     * @param token token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("username").toString();
    }

    /**
     * 从token中获取是否管理员
     *
     * @param token token
     * @return 是否管理员
     */
    public Boolean isAdminFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Boolean.valueOf(claims.get("isAdmin").toString());
    }

    /**
     * 验证token是否过期
     *
     * @param token token
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        Claims claims = getClaimsFromToken(token);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    /**
     * 验证token是否有效
     *
     * @param token token
     * @return 是否有效
     */
    public Boolean validateToken(String token) {
        try {
            if (isTokenExpired(token)) {
                return false;
            }
            Long userId = getUserIdFromToken(token);
            String storedToken = getTokenFromRedis(userId);
            return token.equals(storedToken);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新token
     *
     * @param oldToken 旧token
     * @return 新token
     */
    public String refreshToken(String oldToken) {
        try {
            // 验证旧token是否有效
            if (!validateToken(oldToken)) {
                return null;
            }

            // 从旧token中获取claims
            Claims claims = getClaimsFromToken(oldToken);

            // 创建新token
            String newToken = createToken(claims);

            // 更新Redis中的token
            Long userId = Long.valueOf(claims.get("userId").toString());
            String redisKey = TOKEN_KEY_PREFIX + userId;
            redisUtil.set(redisKey, newToken, expiration);
            return newToken;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建token
     *
     * @param claims 数据声明
     * @return token
     */
    private String createToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从token中获取数据声明
     *
     * @param token token
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}