package CapstoneProject.Capstoneproject1.domain.meeting.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import CapstoneProject.Capstoneproject1.domain.club.domain.ClubRepository;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.*;
import CapstoneProject.Capstoneproject1.domain.meeting.dto.CreateMeetingRequestDto;
import CapstoneProject.Capstoneproject1.domain.meeting.dto.SearchMeetingResponseDto;
import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final MeetingUserRepository meetingUserRepository;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final ScoreRepository scoreRepository;

    @Transactional
    public ResponseDto createMeeting(CreateMeetingRequestDto createMeetingRequestDto, ServletRequest request) {
        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        Meeting meeting = Meeting.builder()
                .meetingName(createMeetingRequestDto.getMeetingName())
                .meetingType(createMeetingRequestDto.getMeetingType())
                .startDate(createMeetingRequestDto.getStartDate())
                .endDate(createMeetingRequestDto.getEndDate())
                .number(1)
                .description(createMeetingRequestDto.getDescription())
                .clubId(user.getClub().getClubId())
                .build();

        meetingRepository.save(meeting);

        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setMeeting(meeting);
        meetingUser.setUser(user);

        meetingUserRepository.save(meetingUser);

        return new ResponseDto("SUCCESS",meeting.getMeetingId()); // 생성된 "meeting"의 id값 반환
    }

    public ResponseDto searchMeeting(Long groupId, ServletRequest request){

        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        List<Meeting> meetingList =  meetingRepository.findAllByClubId(groupId); // 동아리에 생성된 모든 모임

        LocalDateTime now = LocalDateTime.now(); // 현재 시간

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // "dateTime" 형식

        List<SearchMeetingResponseDto> result = new ArrayList<>();

        for(Meeting m : meetingList){
            int compare;
            SearchMeetingResponseDto temp = new SearchMeetingResponseDto();


            LocalDateTime meetingDateTime = LocalDateTime.parse(m.getStartDate(),format);


            compare = meetingDateTime.compareTo(now);
            if(compare == 0 || compare>0){
                temp.setName(m.getMeetingName());
                temp.setType(m.getMeetingType());
                temp.setStartTime(m.getStartDate());

                result.add(temp);
            }

        }
            return new ResponseDto("SUCCESS",result);
    }

    public ResponseDto getMeetingInfo(Long meetingId) {

        Meeting meeting = meetingRepository.getById(meetingId);

        CreateMeetingRequestDto result = new CreateMeetingRequestDto();
        result.setMeetingName(meeting.getMeetingName());
        result.setMeetingType(meeting.getMeetingType());
        result.setStartDate(meeting.getStartDate());
        result.setEndDate(meeting.getEndDate());
        result.setDescription(meeting.getDescription());

        return new ResponseDto("SUCCESS",result);

    }

    public ResponseDto joinMeeting(Long meetingId, ServletRequest request) {

        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 모임입니다."));

        Club club = clubRepository.findById(user.getClub().getClubId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));

        Score score = scoreRepository.findByClub(club);

        if(meeting.getMeetingType().equals("정기모임")){
            score.setRegularScore(score.getRegularScore()+10);
            score.setTotalScore(score.getTotalScore()+10);
            scoreRepository.save(score);
        }

        else if(meeting.getMeetingType().equals("번개모임")){
            score.setImpromptuScore(score.getImpromptuScore()+5);
            score.setTotalScore(score.getTotalScore()+5);
            scoreRepository.save(score);
        }

        else if(meeting.getMeetingType().equals("총회모임")){
            score.setOpeningScore(score.getOpeningScore()+30);
            score.setTotalScore(score.getTotalScore()+30);
            scoreRepository.save(score);
        }

        else{
            return new ResponseDto("FAIL","모임 유형을 다시 확인해주세요");
        }

        meeting.setNumber(meeting.getNumber()+1);
        meetingRepository.save(meeting); // 모임 인원 증가

        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setMeeting(meeting);
        meetingUser.setUser(user);
        meetingUserRepository.save(meetingUser);

        return new ResponseDto("SUCCESS",meeting.getMeetingId());
    }

    public ResponseDto getMeetingHistory(ServletRequest request) {
        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        List<MeetingUser> meetingUserList = meetingUserRepository.findAllByUser(user);

        List<SearchMeetingResponseDto> result = new ArrayList<>();

        for(MeetingUser m : meetingUserList){
            SearchMeetingResponseDto temp = new SearchMeetingResponseDto();
            temp.setName(m.getMeeting().getMeetingName());
            temp.setType(m.getMeeting().getMeetingType());
            temp.setStartTime(m.getMeeting().getStartDate());

            result.add(temp);
        }

        return new ResponseDto("SUCCESS",result);
    }
}