package CapstoneProject.Capstoneproject1.domain.club.controller;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.dto.CreateClubRequestDto;
import CapstoneProject.Capstoneproject1.domain.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("user/group/create")
    @ResponseBody
    public ResponseDto createClub(@RequestBody CreateClubRequestDto createClubRequestDto){
        return clubService.createClub(createClubRequestDto);
    }
}
