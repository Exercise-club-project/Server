package CapstoneProject.Capstoneproject1.domain.meeting.dto;

import lombok.Data;

@Data
public class QrCodeResponseDto {
    private String qrcodeToken;
    private Long expiredTime;
    private Long userId;
}
