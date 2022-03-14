package CapstoneProject.Capstoneproject1.domain.user.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import CapstoneProject.Capstoneproject1.domain.user.dto.UserRegisterDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        .birthDate(userRegisterDto.getBirth_date())
        .phoneNumber(userRegisterDto.getPhone_number())
        .role(userRegisterDto.getRole())
        .build());

        return new ResponseDto("SUCCESS", user.getUserId());
    }
}
