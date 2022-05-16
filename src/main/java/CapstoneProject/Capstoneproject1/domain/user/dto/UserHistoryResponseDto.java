package CapstoneProject.Capstoneproject1.domain.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserHistoryResponseDto {
    private Long userId;
    private String userName;
    private String schoolName;
    private String clubName;
    private Integer totalScore;
    private Integer openScore;
    private Integer regularScore;
    private Integer impromptuScore;

    @Builder
    public UserHistoryResponseDto(Long userId, String userName,String schoolName, String clubName, Integer totalScore,
                                  Integer openScore, Integer regularScore, Integer impromptuScore){
        this.userId = userId;
        this.userName = userName;
        this.schoolName = schoolName;
        this.clubName = clubName;
        this.totalScore = totalScore;
        this.openScore = openScore;
        this.regularScore = regularScore;
        this.impromptuScore = impromptuScore;
    }
}
