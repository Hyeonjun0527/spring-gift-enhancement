package gift.util;

import gift.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String generateAccessToken(Member member) {
        return Jwts.builder()
            .setSubject(member.getId().toString())
            .claim("email", member.getEmail())
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

    public Claims parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody(); // 파싱되면 유효한 토큰
            return claims;
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }
}
