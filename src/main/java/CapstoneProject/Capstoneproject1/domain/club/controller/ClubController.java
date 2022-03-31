package CapstoneProject.Capstoneproject1.domain.club.controller;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.dto.CreateClubRequestDto;
import CapstoneProject.Capstoneproject1.domain.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@Controller
@RequiredArgsConstructor
public class ClubController {

    private final ClubService clubService;

    @PostMapping("user/group/create")
    @ResponseBody
    public ResponseDto createClub(@RequestBody CreateClubRequestDto createClubRequestDto){
        return clubService.createClub(createClubRequestDto);
    }

    @GetMapping("/user/group/search/{school}")
    @ResponseBody
    public ResponseDto searchClub(@PathVariable String school){
        return clubService.searchClub(school);
    }

    @GetMapping("/group/get/{groupId}")
    @ResponseBody
    public ResponseDto getClub(@PathVariable Long groupId){
        return clubService.getClub(groupId);
    }
}
