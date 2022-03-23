package CapstoneProject.Capstoneproject1.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserHistoryResponseDto {
    private String userName;
    private String schoolName;
    private String clubName;

    @Builder
    public UserHistoryResponseDto(String userName, String schoolName, String clubName ){
        this.userName = userName;
        this.schoolName = schoolName;
        this.clubName = clubName;
    }
}
