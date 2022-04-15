package CapstoneProject.Capstoneproject1.domain.rank.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import CapstoneProject.Capstoneproject1.domain.club.domain.ClubRepository;
import CapstoneProject.Capstoneproject1.domain.rank.dto.GetRankByGroupResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RankService {

    private final ClubRepository clubRepository;

    public ResponseDto getRankByGroup() {

        List<Club> clubList = clubRepository.findAll(Sort.by(Sort.Direction.DESC, "totalScore"));
        List<GetRankByGroupResponseDto> result = new ArrayList<>();

        for(Club c : clubList){
            GetRankByGroupResponseDto temp = new GetRankByGroupResponseDto();
            temp.setClub(c.getClubName());
            temp.setSchool(c.getSchool());
            temp.setScore(c.getTotalScore());

            result.add(temp);
        }

        return new ResponseDto("SUCCESS",result);
    }


}
