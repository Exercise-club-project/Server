package CapstoneProject.Capstoneproject1.domain.club.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import CapstoneProject.Capstoneproject1.domain.club.domain.ClubRepository;
import CapstoneProject.Capstoneproject1.domain.club.dto.CreateClubRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;

    public ResponseDto createClub(CreateClubRequestDto createClubRequestDto) {

        if(clubRepository.existsByClubName(createClubRequestDto.getClubName())){
            return new ResponseDto("FAIL","이미 존재하는 동아리 이름입니다.");
        }

        Club club = Club.builder()
                .clubName(createClubRequestDto.getClubName())
                .school(createClubRequestDto.getSchool())
                .peopleNumber(createClubRequestDto.getPeopleNumber())
                .leader(createClubRequestDto.getLeader())
                .build();

        clubRepository.save(club);

        return new ResponseDto("SUCCESS",club.getClubId());
    }
}
