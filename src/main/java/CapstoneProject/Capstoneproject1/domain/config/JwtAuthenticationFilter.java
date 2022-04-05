package CapstoneProject.Capstoneproject1.domain.config;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import org.springframework.security.core.Authentication;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    private RedisTemplate redisTemplate;

    public JwtAuthenticationFilter(JwtAuthenticationProvider jwtAuthenticationProvider, RedisTemplate redisTemplate){
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // "Request Header"에 담긴 "Access_Token"
        String accessToken = jwtAuthenticationProvider.resolveToken(request);

        if(accessToken != null && jwtAuthenticationProvider.validateToken(accessToken)){

            // "Redis"에 존재하는 Black List Access_Token
            String isLogout = (String)redisTemplate.opsForValue().get(accessToken);
            if (ObjectUtils.isEmpty(isLogout)){
                // 토큰이 유효하거나 "Black List Access_Token"이 아닐 경우 인증 성공
                Authentication authentication = jwtAuthenticationProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
