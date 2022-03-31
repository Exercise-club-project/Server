package CapstoneProject.Capstoneproject1.domain.club.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import CapstoneProject.Capstoneproject1.domain.club.domain.ClubRepository;
import CapstoneProject.Capstoneproject1.domain.club.dto.CreateClubRequestDto;
import CapstoneProject.Capstoneproject1.domain.club.dto.GetClubResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
                .leader(createClubRequestDto.getLeader())
                .build();

        clubRepository.save(club);

        return new ResponseDto("SUCCESS",club.getClubId());
    }

    public ResponseDto searchClub(String school) {
        List<CreateClubRequestDto> result = new ArrayList<>();

        List<Club> temp1 = clubRepository.findAllBySchool(school);


        for(Club c : temp1){
            CreateClubRequestDto temp2 = new CreateClubRequestDto();
            temp2.setClubName(c.getClubName());
            temp2.setLeader(c.getLeader());
            temp2.setSchool(c.getSchool());

            result.add(temp2);
        }

        return new ResponseDto("SUCCESS",result);

    }

    public ResponseDto getClub(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 동아리입니다."));

        GetClubResponseDto result = new GetClubResponseDto();
        result.setClubName(club.getClubName());
        result.setLeader(club.getLeader());
        result.setSchool(club.getSchool());
        result.setNumber(club.getPeopleNumber());

        return new ResponseDto("SUCCESS",result);
    }
}
