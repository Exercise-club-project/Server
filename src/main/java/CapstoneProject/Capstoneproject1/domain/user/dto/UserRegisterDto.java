package CapstoneProject.Capstoneproject1.domain.user.dto;

import lombok.Data;

@Data
public class UserRegisterDto {

    private String email;
    private String password;
    private String name;
    private String birth_date;
    private String sex;
    private String phone_number;
    private String role;
}
