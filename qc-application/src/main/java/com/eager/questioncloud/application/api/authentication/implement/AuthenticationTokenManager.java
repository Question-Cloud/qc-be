package com.eager.questioncloud.application.api.authentication.implement;

import com.eager.questioncloud.application.api.authentication.dto.AuthenticationToken;
import com.eager.questioncloud.core.domain.token.RefreshTokenRepository;
import com.eager.questioncloud.core.exception.CoreException;
import com.eager.questioncloud.core.exception.Error;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationTokenManager {
    private final Key secretKey;
    private final RefreshTokenRepository refreshTokenRepository;
    public static final Long ACCESS_TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L;
    public static final Long REFRESH_TOKEN_VALID_TIME = 1000L * 60L * 60L * 24L * 30L;

    public AuthenticationTokenManager(@Value("${JWT_SECRET_KEY}") String secretKey, RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public AuthenticationToken create(Long uid) {
        String accessToken = generateAccessToken(uid);
        String refreshToken = generateRefreshToken(uid);
        return AuthenticationToken.create(accessToken, refreshToken);
    }

    public AuthenticationToken refresh(String refreshToken) {
        Claims claims = getRefreshTokenClaimsWithValidate(refreshToken);
        Long uid = claims.get("uid", Long.class);
        String accessToken = generateAccessToken(uid);
        if (isExpireSoon(claims.getExpiration())) {
            return AuthenticationToken.create(accessToken, generateRefreshToken(uid));
        }
        return AuthenticationToken.create(accessToken, refreshToken);
    }

    public Long parseUidFromAccessToken(String accessToken) {
        Claims claims = getClaims(accessToken);

        if (!claims.getSubject().equals("accessToken")) {
            throw new CoreException(Error.UNAUTHORIZED_TOKEN);
        }

        return claims.get("uid", Long.class);
    }

    public Claims getClaims(String token) {
        try {
            return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            throw new CoreException(Error.UNAUTHORIZED_TOKEN);
        }
    }

    private String generateAccessToken(Long uid) {
        Claims claims = Jwts.claims().setSubject("accessToken");
        claims.put("uid", uid);
        Date currentTime = new Date();

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(currentTime)
            .setExpiration(new Date(currentTime.getTime() + ACCESS_TOKEN_VALID_TIME))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    private String generateRefreshToken(Long uid) {
        Claims claims = Jwts.claims().setSubject("refreshToken");
        claims.put("uid", uid);
        Date currentTime = new Date();

        String refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(currentTime)
            .setExpiration(new Date(currentTime.getTime() + REFRESH_TOKEN_VALID_TIME))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();

        refreshTokenRepository.save(refreshToken, uid);
        return refreshToken;
    }

    private Claims getRefreshTokenClaimsWithValidate(String refreshToken) {
        Claims claims = getClaims(refreshToken);

        if (!claims.getSubject().equals("refreshToken")) {
            throw new CoreException(Error.UNAUTHORIZED_TOKEN);
        }

        Long uid = claims.get("uid", Long.class);
        String refreshTokenInStore = refreshTokenRepository.getByUserId(uid);

        if (!refreshToken.equals(refreshTokenInStore)) {
            throw new CoreException(Error.UNAUTHORIZED_TOKEN);
        }

        return claims;
    }

    private Boolean isExpireSoon(Date expireDate) {
        Date now = new Date();
        long differenceInMillis = expireDate.getTime() - now.getTime();
        long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
        return differenceInDays <= 7;
    }
}
