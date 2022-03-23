package CapstoneProject.Capstoneproject1.domain.user.dto;

import lombok.Data;

@Data
public class UserLogoutRequestDto {
    private String email;
    private String accessToken;
    private String refreshToken;
}
