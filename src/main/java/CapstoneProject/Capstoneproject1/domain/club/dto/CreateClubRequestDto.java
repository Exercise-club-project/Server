package CapstoneProject.Capstoneproject1.domain.club.dto;

import lombok.Data;

@Data
public class CreateClubRequestDto {
    private String clubName;
    private String school;
    private Integer peopleNumber;
    private String leader;
}