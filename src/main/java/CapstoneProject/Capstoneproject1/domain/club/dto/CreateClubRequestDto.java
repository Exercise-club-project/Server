package CapstoneProject.Capstoneproject1.domain.club.dto;

import lombok.Data;

@Data
public class CreateClubRequestDto {
    private Long clubId;
    private String clubName;
    private String school;
    private String leader;
}