package CapstoneProject.Capstoneproject1.domain.club.dto;

import lombok.Data;

@Data
public class GetClubResponseDto {
    private String clubName;
    private String school;
    private Integer number;
    private Integer totalScore;
    private Integer openScore;
    private Integer regularScore;
    private Integer impromptuScroe;
}
