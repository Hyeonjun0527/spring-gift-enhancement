package gift.security;

import gift.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=".getBytes(StandardCharsets.UTF_8)
    );

    public String createToken(Member member) {
        if (member.getId() == null) {
            throw new IllegalStateException("회원 ID가 없는 상태에서는 토큰을 발급할 수 없습니다.");
        }

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("email", member.getEmail())
                .signWith(KEY)
                .compact();
    }

    public void validateToken(String token) {
        Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);
    }
    public String getSubject(String token) {
        return Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
