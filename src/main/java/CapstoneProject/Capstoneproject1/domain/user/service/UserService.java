package CapstoneProject.Capstoneproject1.domain.user.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.meeting.dto.QrCodeResponseDto;
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
        if(userRepository.existsByEmail(userRegisterDto.getEmail())){
            return new ResponseDto("FAIL","이미 가입되어있는 회원입니다.");
        }
        User user = userRepository.save(User.builder()
                .email(userRegisterDto.getEmail())
                .password(passwordEncoder.encode(userRegisterDto.getPassword()))
                .name(userRegisterDto.getName())
                .sex(userRegisterDto.getSex())
                .birthDate(userRegisterDto.getBirthDate())
                .phoneNumber(userRegisterDto.getPhoneNumber())
                .roles(Collections.singletonList("ROLE_USER"))
                .totalScore(0)
                .impromptuScore(0)
                .openingScore(0)
                .regularScore(0)
                .grade("일반회원")
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
        TokenDto tokenDto = jwtAuthenticationProvider.createToken(user.getEmail(), user.getRoles());

        // 생성된 "RefreshToken"를 "Redis"에 저장, 시간이 만료 되면 자동적으로 삭제
        // key 값: "RT:email",
        redisTemplate.opsForValue().set("RT:"+user.getEmail(),
                tokenDto.getRefreshToken(), tokenDto.getRefreshTokenTime(), TimeUnit.MILLISECONDS);

        LoginDto loginDto = new LoginDto();
        loginDto.setGrade(user.getGrade());
        loginDto.setTokenDto(tokenDto);

        if(user.getClub() == null){
            loginDto.setClubId(0L); // 회원이 동아리에 속해있지 않음
            return new ResponseDto("SUCCESS",loginDto);
        }
        loginDto.setClubId(user.getClub().getClubId()); // 회원이 동아리에 속해있음
        return new ResponseDto("SUCCESS", loginDto);

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


    public ResponseDto createQrcodeToken(ServletRequest request) {
        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        if(redisTemplate.opsForValue().get("QR:" + user.getEmail()) != null) {
            redisTemplate.delete("QR:"+user.getEmail());

            QrCodeResponseDto tokenDto = jwtAuthenticationProvider.createQrCodeToken(user.getEmail(), user.getRoles());
            tokenDto.setUserId(user.getUserId());

            redisTemplate.opsForValue().set("QR:" + user.getEmail(),
                    tokenDto.getQrcodeToken(), tokenDto.getExpiredTime(), TimeUnit.MILLISECONDS);
            // "Redis"에 "QRCODE"가 이미 존재 할 경우 삭제 후 재전송
            return new ResponseDto("SUCCESS", tokenDto);
        }
        QrCodeResponseDto tokenDto = jwtAuthenticationProvider.createQrCodeToken(user.getEmail(), user.getRoles());
        tokenDto.setUserId(user.getUserId());

        redisTemplate.opsForValue().set("QR:" + user.getEmail(),
                tokenDto.getQrcodeToken(), tokenDto.getExpiredTime(), TimeUnit.MILLISECONDS);
        return new ResponseDto("SUCCESS", tokenDto.getQrcodeToken());
    }

    public ResponseDto getQrcodeToken(ServletRequest request) {
        String token = jwtAuthenticationProvider.resolveQrCodeToken((HttpServletRequest) request);
        if(!jwtAuthenticationProvider.validateToken(token)){
            return new ResponseDto("FAIL","QR 코드를 재갱신 해주세요");
            // 토큰이 만료될 경우
        }
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        return new ResponseDto("SUCCESS",user.getUserId());
    }

    public ResponseDto getUserHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원입니다."));

        if(user.getClub() == null){
            return new ResponseDto("FAIL", "해당 사용자는 동아리에 가입되어 있지 않습니다.");
        }

        UserHistoryResponseDto userHistoryResponseDto = UserHistoryResponseDto.builder()
                .userName(user.getUsername())
                .schoolName(user.getClub().getSchool())
                .clubName(user.getClub().getClubName())
                .totalScore(user.getTotalScore())
                .openScore(user.getOpeningScore())
                .regularScore(user.getRegularScore())
                .impromptuScore(user.getImpromptuScore())
                .build();

        return new ResponseDto("SUCCESS",userHistoryResponseDto);
    }
}