package CapstoneProject.Capstoneproject1.domain.club.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.dto.CreateClubRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {

    public ResponseDto createClub(CreateClubRequestDto createClubRequestDto) {
        return new ResponseDto();
    }
}
