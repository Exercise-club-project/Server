package CapstoneProject.Capstoneproject1.domain.rank.dto;

import lombok.Data;

@Data
public class GetRankByUserResponseDto {
    private Integer num;
    private String name;
    private String club;
    private Integer score;
}
