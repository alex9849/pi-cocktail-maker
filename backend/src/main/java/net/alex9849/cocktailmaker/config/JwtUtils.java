package net.alex9849.cocktailmaker.config;

import io.jsonwebtoken.*;
import net.alex9849.cocktailmaker.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${alex9849.app.jwtSecret}")
    private String jwtSecret;

    @Value("${alex9849.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication, boolean remember) {
        User principal = (User) authentication.getPrincipal();
        Date expirationDate;
        if(remember) {
            long tenYears = 10L * 365 * 24 * 60 * 60 * 1000;
            expirationDate = new Date(new Date().getTime() + tenYears);
        } else {
            expirationDate = new Date((new Date()).getTime() + jwtExpirationMs);
        }

        return Jwts.builder()
                .setSubject(String.valueOf(principal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim("remember", remember)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public long getUserIdFromJwtToken(String token) {
        return Long.parseLong(Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject());
    }

    public Date getExpirationDateFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getExpiration();
    }

    public boolean isRemember(String token) {
        return (boolean) Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("remember");
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (SignatureException e) {
            logger.error("Untrusted JWT token: {}", e.getMessage());
        }

        return false;
    }

    public String parseJwt(@Nullable String authHeaderContent) {
        if (StringUtils.hasText(authHeaderContent) && authHeaderContent.startsWith("Bearer ")) {
            return authHeaderContent.substring(7, authHeaderContent.length());
        }
        return null;
    }
}
