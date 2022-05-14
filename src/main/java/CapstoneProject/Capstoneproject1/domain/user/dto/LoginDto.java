package CapstoneProject.Capstoneproject1.domain.user.dto;

import lombok.Data;

@Data
public class LoginDto {
    private Long clubId; // 회원이 동아리에 속해있을 경우 회원의 groupId, 속해있지 않을 경우 0
    private String grade; // 회원 등급
    private TokenDto tokenDto;
}
