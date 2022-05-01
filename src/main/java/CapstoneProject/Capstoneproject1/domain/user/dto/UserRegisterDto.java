package CapstoneProject.Capstoneproject1.domain.user.dto;

import lombok.Data;

@Data
public class UserRegisterDto {

    private String email;
    private String password;
    private String name;
    private String school;
    private String birthDate;
    private String sex;
    private String phoneNumber;
}
