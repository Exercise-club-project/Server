package CapstoneProject.Capstoneproject1.domain.user.controller;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.user.dto.UserRegisterDto;
import CapstoneProject.Capstoneproject1.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("auth/register")
    @ResponseBody
    public ResponseDto userRegister(@RequestBody UserRegisterDto userRegisterDto){
        return userService.userRegister(userRegisterDto);
    }

    @GetMapping("auth/login")
    public ResponseDto login(){
        return new ResponseDto("SUCCESS");
    }
}
