package ru.example.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.example.model.User;

import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = Logger.getLogger(JwtTokenProvider.class);
    private static final long EXPIRE_DURATION = 24 * 60 * 60 * 1000; // 24 hour

    @Value("${app.jwt.secret}")
    private String SECRET_KEY;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(user.getLogin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error(String.format("JWT истёк. %s", e.getMessage()), e);
        } catch (IllegalArgumentException e) {
            logger.error(String.format("Токен пуст или равен null. %s", e.getMessage()), e);
        } catch (MalformedJwtException e) {
            logger.error(String.format("JWT некорректен. %s", e.getMessage()), e);
        } catch (UnsupportedJwtException e) {
            logger.error(String.format("JWT не поддерживается. %s", e.getMessage()), e);
        } catch (SignatureException e) {
            logger.error(String.format("Проверка подлинности не прошла. %s", e.getMessage()), e);
        }

        return false;
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}