package CapstoneProject.Capstoneproject1.domain.rank.dto;

import lombok.Data;

@Data
public class GetRankByGroupResponseDto {

    private Integer num;
    private String club;
    private Long clubId;
    private String school;
    private Integer score;

}
