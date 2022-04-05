package CapstoneProject.Capstoneproject1.domain.config;

import CapstoneProject.Capstoneproject1.domain.user.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationProvider {
    private String secretKey = "zoqtmxhselwkdls1zoqtmxhselwkdls1zoqtmxhselwkdls1zoqtmxhselwkdls1";

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일

    @Autowired
    private UserDetailsService userDetailsService;

    // JWT 토큰 생성
    public TokenDto createToken(String userEmail, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userEmail); // JWT payload 에 저장되는 정보단위
        // "setSubject"를 통해서 "userEmail"을 넣음, 기존에는 이름이였음
        claims.put("roles", roles); // 정보는 key / value 쌍으로 저장된다.
        Date now1 = new Date();
        long now2 = new Date().getTime();

        Date accessTokenDate = new Date(now2 + ACCESS_TOKEN_EXPIRE_TIME);
        Date refreshTokenDate = new Date(now2 + REFRESH_TOKEN_EXPIRE_TIME);

        String accessToken =  Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now1) // 토큰 발행 시간 정보
                .setExpiration(accessTokenDate) // set Expire Time, 토큰 유효 기간 설정
                .signWith(getSigninKey(secretKey), SignatureAlgorithm.HS256)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();

        String refreshToken =  Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now1) // 토큰 발행 시간 정보
                .setExpiration(refreshTokenDate) // 만료 "날짜(시간이 아니라 날짜)", 토큰 유효 기간 설정
                .signWith(getSigninKey(secretKey), SignatureAlgorithm.HS256)  // 사용할 암호화 알고리즘과 signature 에 들어갈 secret값 세팅
                .compact();

        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(accessToken);
        tokenDto.setAccessTokenTime(ACCESS_TOKEN_EXPIRE_TIME);
        tokenDto.setRefreshToken(refreshToken);
        tokenDto.setRefreshTokenTime(REFRESH_TOKEN_EXPIRE_TIME);

        return tokenDto;
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigninKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    // "Request"의 "Header"에서 "Access_Token" 값을 가져옵니다. "Access_Token" : "Access_Token"의 값
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Access_Token");
    }

    // 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigninKey(secretKey)).build().parseClaimsJws(jwtToken);
            return true;
        }
        catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            return false;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            return false;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            return false;
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            return false;
        }
    }

    private Key getSigninKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getExpiration(String accessToken){
        // "AccessToken"의 유효 시간
        Date expiration = Jwts.parserBuilder().setSigningKey(getSigninKey(secretKey)).build()
                .parseClaimsJws(accessToken).getBody().getExpiration();
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}