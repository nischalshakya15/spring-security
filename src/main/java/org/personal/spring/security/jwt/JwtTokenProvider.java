package org.personal.spring.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.personal.spring.security.config.UserPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.refreshToken.secretKey}")
    private String refreshTokenSecretKey;

    @Value("${jwt.accessToken.secretKey}")
    private String accessTokenSecretKey;

    @Value("${jwt.refreshToken.expiresIn}")
    private Long refreshTokenExpireInMs;

    @Value("${jwt.accessToken.expiresIn}")
    private Long accessTokenExpireInMs;

    public String generateAccessToken(Authentication authentication) {
        return getToken(authentication, accessTokenExpireInMs, "Access token expiry date {} of user {} ", accessTokenSecretKey);
    }

    public String generateRefreshToken(Authentication authentication) {
        return getToken(authentication, refreshTokenExpireInMs, "Refresh token expiry date {} of user {} ", refreshTokenSecretKey);
    }

    private String getToken(Authentication authentication, Long tokenExpiryTime, String tokenMessage, String tokenSecretKEy) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenExpiryTime);
        log.info(tokenMessage, expiryDate, userPrincipal.getUsername());
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(expiryDate)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, tokenSecretKEy)
                .compact();
    }

    public Long getUserIdFromAccessToken(String accessToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(accessTokenSecretKey)
                .parseClaimsJws(accessToken)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }


    public Long getUserIdFromRefreshToken(String refreshToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(refreshTokenSecretKey)
                .parseClaimsJws(refreshToken)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateAccessToken(String accessToken)
            throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedOperationException, IllegalArgumentException {
        Jwts.parser().setSigningKey(accessTokenSecretKey).parseClaimsJws(accessToken);
        return true;
    }

    public boolean validateRefreshToken(String refreshToken)
            throws SignatureException, MalformedJwtException, ExpiredJwtException, UnsupportedOperationException, IllegalArgumentException {
        Jwts.parser().setSigningKey(refreshTokenSecretKey).parseClaimsJws(refreshToken);
        return true;
    }

}
