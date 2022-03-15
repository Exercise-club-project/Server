package CapstoneProject.Capstoneproject1.domain.user.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import CapstoneProject.Capstoneproject1.domain.user.dto.UserLoginDto;
import CapstoneProject.Capstoneproject1.domain.user.dto.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;

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
        if(!userRepository.existsByEmail(userLoginDto.getEmail())){
            return new ResponseDto("FAIL","존재하지 않는 이메일입니다.");
        }
        User user = userRepository.findByEmail(userLoginDto.getEmail());
        if(!passwordEncoder.matches(userLoginDto.getPassword(),user.getPassword())){
            return new ResponseDto("FAIL","비밀번호가 틀렸습니다.");
        }
        String token = jwtAuthenticationProvider.createToken(user.getName(),user.getRoles());
        return new ResponseDto("SUCCESS",token);
    }
}