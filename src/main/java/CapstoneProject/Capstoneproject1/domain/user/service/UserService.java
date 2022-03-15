package CapstoneProject.Capstoneproject1.domain.user.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import CapstoneProject.Capstoneproject1.domain.user.dto.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
        .role("회원")
        .build());

        return new ResponseDto("SUCCESS", user.getUserId());
    }

    public ResponseDto checkEmail(String email) {
        boolean result = userRepository.existsByEmail(email);
        if(result){
            return new ResponseDto("SUCCESS","이메일이 중복됩니다.");
        }
        return new ResponseDto("SUCCESS","사용가능한 이메일입니다.");
    }
}