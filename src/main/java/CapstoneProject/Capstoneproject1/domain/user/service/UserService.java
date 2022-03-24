package CapstoneProject.Capstoneproject1.domain.user.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import CapstoneProject.Capstoneproject1.domain.user.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate redisTemplate;
    private final UserDetailsService userDetailsService;


    @Transactional
    public ResponseDto userRegister(UserRegisterDto userRegisterDto) {
        User user = userRepository.save(User.builder()
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .name(userRegisterDto.getName())
                .sex(userRegisterDto.getSex())
                .birthDate(userRegisterDto.getBirthDate())
                .phoneNumber(userRegisterDto.getPhoneNumber())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        return new ResponseDto("SUCCESS", user.getUserId());
    }

    public ResponseDto checkEmail(String email) {
        boolean result = userRepository.existsByEmail(email);
        if (result) {
            return new ResponseDto("FAIL", "이메일이 중복됩니다.");
        }
        return new ResponseDto("SUCCESS", "사용가능한 이메일입니다.");
    }

    public ResponseDto login(UserLoginDto userLoginDto) {

        if (!userRepository.existsByEmail(userLoginDto.getEmail())) {
            return new ResponseDto("FAIL", "존재하지 않는 이메일입니다.");}

        User user = userRepository.findByEmail(userLoginDto.getEmail());

        if (!passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())) {
                return new ResponseDto("FAIL", "비밀번호가 틀렸습니다.");
            }
        // 로그인 할 경우 "AccessToken"과 "RefreshToken"을 "TokenDto"에 넣어 반환
        TokenDto tokenDto = jwtAuthenticationProvider.createToken(user.getName(), user.getRoles());

        // 생성된 "RefreshToken"를 "Redis"에 저장, 시간이 만료 되면 자동적으로 삭제
        // key 값: "RT:email",
        redisTemplate.opsForValue().set("RT:"+user.getEmail(),
                tokenDto.getRefreshToken(), tokenDto.getRefreshTokenTime(), TimeUnit.MILLISECONDS);

        return new ResponseDto("SUCCESS", tokenDto);
        }
    public ResponseDto logout(ServletRequest request, UserLogoutRequestDto userLogoutRequestDto){
        // 1. Access Token 검증
        if(!jwtAuthenticationProvider.validateToken(userLogoutRequestDto.getAccessToken())){
            return new ResponseDto("FAIL","잘못된 요청입니다.");
        }

        // 2. "Access Token"을 이용해서 User(사용자를) 찾음
        // "request" 헤더에는 "Access Token"이 존재, resolveToken 메소드의 인자에 "AccessToken" 값을 넣어줌
        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = (User) userDetailsService.loadUserByUsername(jwtAuthenticationProvider.getUserPk(token));

        // 3. "Redis"에서 해당 User email 로 저장된 Refresh Token 이 있는지 여부를 확인 후 있을 경우 삭제합니다.
        if (redisTemplate.opsForValue().get("RT:" + user.getEmail()) != null) {
            // Refresh Token 삭제
            redisTemplate.delete("RT:"+user.getEmail());
        }

        // 4. 해당 Access Token 유효시간 가지고 와서 "BlackList"로 저장하기, "BlackList"에 존재하는 토큰은 차단
        // "key" 값은 토큰 값, "value" 값에 "logout"을 넣어주고 그 기간동안 사용자가 접근 시 이를 이용해서 사용자의 접근을 차단
        Long expiration = jwtAuthenticationProvider.getExpiration(userLogoutRequestDto.getAccessToken());
        redisTemplate.opsForValue()
                .set(userLogoutRequestDto.getAccessToken(), "logout", expiration, TimeUnit.MILLISECONDS);

        // 로그아웃된 사용자의 id 값 반환
        return new ResponseDto("SUCCESS",user.getUserId());
    }

    // "Access Token"이 만료될 경우 "Refresh Token"을 통하여 "Access Token" 재발급
    public ResponseDto reIssue(UserLogoutRequestDto userLogoutRequestDto, ServletRequest request){
        // 1.기존 "Access Token"을 이용하여 사용자 조회
        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = (User) userDetailsService.loadUserByUsername(jwtAuthenticationProvider.getUserPk(token));

        // 2. 사용자의 "email"값을 이용하여 "Redis"에 저장된 사용자의 "Refresh Token" 조회
        String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + user.getEmail());

        // 3. 새로운 토큰 생성
        TokenDto tokenDto = jwtAuthenticationProvider.createToken(user.getName(), user.getRoles());

        // 4. "Redis"에 존재하는 "Refresh Token" 업데이트(재갱신)
        redisTemplate.opsForValue().set("RT:"+user.getEmail(),
                tokenDto.getRefreshToken(), tokenDto.getRefreshTokenTime(), TimeUnit.MILLISECONDS);

        return new ResponseDto("SUCCESS",tokenDto);
    }


}