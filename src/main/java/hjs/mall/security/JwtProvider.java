package hjs.mall.security;

import hjs.mall.domain.Role;
import hjs.mall.repository.MemberJpaRepository;
import hjs.mall.service.MemberService;
import hjs.mall.service.UserDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;

@RequiredArgsConstructor
@Component
@Log4j2
public class JwtProvider {

    @Value("${jwt.secret.accessKey}")
    private String access_salt;

    @Value("${jwt.secret.refreshKey}")
    private String refresh_salt;

    private Key secretAccessKey;
    private Key secretRefreshKey;

    // expiry duration : 30min
    private final long exp = 1000L * 60 * 30;

    private final UserDetailService userDetailService;

    @PostConstruct
    protected void init() {
        secretAccessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(access_salt));
        secretRefreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refresh_salt));
    }

    // Create AccessToken
    public String createAccessToken(String userId, Role role) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + exp))
                .signWith(secretAccessKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String userId, Role role) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", role);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofDays(30).toMillis()))
                .signWith(secretRefreshKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //  Get Authorization Info
    //  Spring Security to check whether the user is authorized or not
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailService.loadUserByUsername(this.getAccount(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //  get the userId From token
    public String getAccount(String token) {
        return Jwts.parserBuilder().setSigningKey(secretAccessKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Authorization Header
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // Validate Token
    public boolean validateToken(String token) {
        try {
            // Validate Bearer
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretAccessKey).build().parseClaimsJws(token);
            // return false when it is expired
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}