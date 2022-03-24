package CapstoneProject.Capstoneproject1.domain.user.controller;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import CapstoneProject.Capstoneproject1.domain.user.dto.UserLoginDto;
import CapstoneProject.Capstoneproject1.domain.user.dto.UserLogoutRequestDto;
import CapstoneProject.Capstoneproject1.domain.user.dto.UserRegisterDto;
import CapstoneProject.Capstoneproject1.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @PostMapping("auth/register")
    @ResponseBody
    public ResponseDto userRegister(@RequestBody UserRegisterDto userRegisterDto){
        return userService.userRegister(userRegisterDto);
    }

    @GetMapping("auth/check/{email}")
    @ResponseBody
    public ResponseDto checkEmail(@PathVariable String email){
        return userService.checkEmail(email);
    }

    @PostMapping("auth/login")
    @ResponseBody
    public ResponseDto login(@RequestBody UserLoginDto userLoginDto){
        return userService.login(userLoginDto);
    }

    @PostMapping("auth/logout")
    @ResponseBody
    public ResponseDto logout(ServletRequest request, @RequestBody UserLogoutRequestDto userLogoutRequestDto){
        return userService.logout(request,userLogoutRequestDto);
    }

    @PostMapping("auth/token/reissue")
    @ResponseBody
    public ResponseDto reIssue(@RequestBody UserLogoutRequestDto userLogoutRequestDto, ServletRequest request){
        return userService.reIssue(userLogoutRequestDto,request);
    }
}