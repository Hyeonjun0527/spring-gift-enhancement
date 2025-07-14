package gift.filter;

import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();

        // wishlist 경로 토큰 검사. 로그인이 되어야 이용 가능한 기능
        if (uri.startsWith("/api/wishes")) {

            String token = request.getHeader("Authorization");

            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token is missing");
                return;
            }

            Claims claims = jwtUtil.parseToken(token);
            if (claims == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid Token");
                return;
            }

            request.setAttribute("loginMemberEmail", claims.get("email", String.class));

            chain.doFilter(req, res);
        } else {
            // wishlist 경로가 아니면 그냥 통과
            chain.doFilter(req, res);
        }
    }


}
