package CapstoneProject.Capstoneproject1.domain.rank.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import CapstoneProject.Capstoneproject1.domain.club.domain.ClubRepository;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.rank.dto.GetRankByGroupResponseDto;
import CapstoneProject.Capstoneproject1.domain.rank.dto.GetRankByUserResponseDto;
import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class RankService {

    private final ClubRepository clubRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserRepository userRepository;

    public ResponseDto getRankByGroup() {
        int num = 1;

        List<Club> clubList = clubRepository.findAll(Sort.by(Sort.Direction.DESC, "totalScore"));
        List<GetRankByGroupResponseDto> result = new ArrayList<>();

        for(Club c : clubList){
            GetRankByGroupResponseDto temp = new GetRankByGroupResponseDto();
            temp.setNum(num++);
            temp.setClub(c.getClubName());
            temp.setClubId(c.getClubId());
            temp.setSchool(c.getSchool());
            temp.setScore(c.getTotalScore());

            result.add(temp);
        }

        return new ResponseDto("SUCCESS",result);
    }


    public ResponseDto getRankByUserInGroup(ServletRequest request) {
        int num = 1;

        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        List<User> userList = userRepository.findAllByClubOrderByTotalScoreDesc(user.getClub());

        List<GetRankByUserResponseDto> result = new ArrayList<>();

        for(User u : userList){
            GetRankByUserResponseDto temp = new GetRankByUserResponseDto();
            temp.setNum(num++);
            temp.setUserId(u.getUserId());
            temp.setClub(u.getClub().getClubName());
            temp.setName(u.getName());
            temp.setScore(u.getTotalScore());
            result.add(temp);
        }

        return new ResponseDto("SUCCESS",result);

    }

    public ResponseDto getRankByUser() {
        int num = 1;

        List<User> userList = userRepository.findAll(Sort.by(Sort.Direction.DESC, "totalScore"));
        List<GetRankByUserResponseDto> result = new ArrayList<>();

        for(User u : userList){
            if(u.getClub() == null){
                new ResponseDto("FAIL","동아리에 가입되어 있지 않은 회원이 존재합니다.");
            }
            GetRankByUserResponseDto temp = new GetRankByUserResponseDto();
            temp.setNum(num++);
            temp.setClub(u.getClub().getClubName());
            temp.setName(u.getName());
            temp.setUserId(u.getUserId());
            temp.setScore(u.getTotalScore());

            result.add(temp);
        }

        return new ResponseDto("SUCCESS",result);
    }
}
