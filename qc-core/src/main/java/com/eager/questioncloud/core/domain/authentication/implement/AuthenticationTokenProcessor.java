package com.eager.questioncloud.core.domain.authentication.implement;

import com.eager.questioncloud.core.domain.authentication.vo.AuthenticationToken;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
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
public class AuthenticationTokenProcessor {
    private final Key secretKey;
    private final RefreshTokenAppender refreshTokenAppender;
    private final RefreshTokenReader refreshTokenReader;
    public static final Long ACCESS_TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L;
    public static final Long REFRESH_TOKEN_VALID_TIME = 1000L * 60L * 60L * 24L * 30L;

    public AuthenticationTokenProcessor(@Value("${JWT_SECRET_KEY}") String secretKey,
        RefreshTokenAppender refreshTokenAppender, RefreshTokenReader refreshTokenReader) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.refreshTokenAppender = refreshTokenAppender;
        this.refreshTokenReader = refreshTokenReader;
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
            throw new CustomException(Error.UNAUTHORIZED_TOKEN);
        }
    }

    public String generateAccessToken(Long uid) {
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

    public String generateRefreshToken(Long uid) {
        Claims claims = Jwts.claims().setSubject("refreshToken");
        claims.put("uid", uid);
        Date currentTime = new Date();

        String refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(currentTime)
            .setExpiration(new Date(currentTime.getTime() + REFRESH_TOKEN_VALID_TIME))
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();

        refreshTokenAppender.append(refreshToken, uid);
        return refreshToken;
    }

    public AuthenticationToken refresh(String refreshToken) {
        Claims claims = getRefreshTokenClaimsWithValidate(refreshToken);
        Long uid = claims.get("uid", Long.class);
        String accessToken = generateAccessToken(uid);
        if (isExpireSoon(claims.getExpiration())) {
            return new AuthenticationToken(accessToken, generateRefreshToken(uid));
        }
        return new AuthenticationToken(accessToken, refreshToken);
    }

    public Claims getAccessTokenClaimsWithValidate(String accessToken) {
        Claims claims = getClaims(accessToken);

        if (!claims.getSubject().equals("accessToken")) {
            throw new CustomException(Error.UNAUTHORIZED_TOKEN);
        }

        return claims;
    }

    public Claims getRefreshTokenClaimsWithValidate(String refreshToken) {
        Claims claims = getClaims(refreshToken);

        if (!claims.getSubject().equals("refreshToken")) {
            throw new CustomException(Error.UNAUTHORIZED_TOKEN);
        }

        Long uid = claims.get("uid", Long.class);
        String refreshTokenInStore = refreshTokenReader.getByUserId(uid);

        if (!refreshToken.equals(refreshTokenInStore)) {
            throw new CustomException(Error.UNAUTHORIZED_TOKEN);
        }

        return claims;
    }

    public Boolean isExpireSoon(Date expireDate) {
        Date now = new Date();
        long differenceInMillis = expireDate.getTime() - now.getTime();
        long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
        return differenceInDays <= 7;
    }
}
