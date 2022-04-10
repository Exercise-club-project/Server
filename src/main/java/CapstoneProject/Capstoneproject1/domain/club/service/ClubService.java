package CapstoneProject.Capstoneproject1.domain.club.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import CapstoneProject.Capstoneproject1.domain.club.domain.ClubRepository;
import CapstoneProject.Capstoneproject1.domain.club.dto.CreateClubRequestDto;
import CapstoneProject.Capstoneproject1.domain.club.dto.GetClubResponseDto;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.Score;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.ScoreRepository;
import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubService {

    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserDetailsService userDetailsService;
    private final ScoreRepository scoreRepository;

    public ResponseDto createClub(CreateClubRequestDto createClubRequestDto, ServletRequest request) {

        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        if(clubRepository.existsByClubName(createClubRequestDto.getClubName())){
            return new ResponseDto("FAIL","이미 존재하는 동아리 이름입니다.");
        }

        Club club = Club.builder()
                .clubName(createClubRequestDto.getClubName())
                .school(createClubRequestDto.getSchool())
                .peopleNumber(1)
                .leader(createClubRequestDto.getLeader())
                .build();

        clubRepository.save(club);

        user.setClub(club);
        userRepository.save(user);

        Score score = new Score();
        score.setTotalScore(0);
        score.setRegularScore(0);
        score.setImpromptuScore(0);
        score.setOpeningScore(0);
        score.setClub(club);
        scoreRepository.save(score);

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

    public ResponseDto joinClub(Long clubId, ServletRequest request) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 동아리입니다."));

        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        user.setClub(club);
        userRepository.save(user); // 유저 동아리 가입

        club.setPeopleNumber(club.getPeopleNumber()+1); // 동아리 회원 수 증가
        clubRepository.save(club);

        return new ResponseDto("SUCCESS",club.getClubId());
    }
}
