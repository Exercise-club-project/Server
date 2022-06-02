package CapstoneProject.Capstoneproject1.domain.meeting.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import CapstoneProject.Capstoneproject1.domain.club.domain.ClubRepository;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.*;
import CapstoneProject.Capstoneproject1.domain.meeting.dto.CreateMeetingRequestDto;
import CapstoneProject.Capstoneproject1.domain.meeting.dto.MeetingInfoResponseDto;
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

        meetingUserRepository.save(meetingUser);

        return new ResponseDto("SUCCESS",meeting.getMeetingId()); // 생성된 "meeting"의 id값 반환
    }

    public ResponseDto searchMeeting(Long groupId, ServletRequest request){

        List<Meeting> meetingList =  meetingRepository.findAllByClubId(groupId); // 동아리에 생성된 모든 모임

        LocalDateTime now = LocalDateTime.now(); // 현재 시간

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // "dateTime" 형식

        List<SearchMeetingResponseDto> result = new ArrayList<>();

        for(Meeting m : meetingList){
            int compare;
            SearchMeetingResponseDto temp = new SearchMeetingResponseDto();

            LocalDateTime meetingDateTime = LocalDateTime.parse(m.getEndDate(),format);

            compare = meetingDateTime.compareTo(now);
            if(compare == 0 || compare>0){
                temp.setMeetingId(m.getMeetingId());
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

        MeetingInfoResponseDto result = new MeetingInfoResponseDto();
        result.setMeetingName(meeting.getMeetingName());
        result.setMeetingType(meeting.getMeetingType());
        result.setStartDate(meeting.getStartDate());
        result.setEndDate(meeting.getEndDate());
        result.setDescription(meeting.getDescription());

        List<String> temp = new ArrayList<>();

        try {
            List<MeetingUser> userList = meetingUserRepository.findAllByMeeting(meeting);

            for(MeetingUser m : userList){
                temp.add(m.getUser().getUsername());
        }
            result.setJoinList(temp);
        }

        catch (NullPointerException e){
            temp.add("참석한 회원이 존재하지 않습니다.");
            result.setJoinList(temp);
        }

        return new ResponseDto("SUCCESS",result);

    }

    public ResponseDto joinMeeting(Long userId, Long meetingId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 모임입니다."));

        Club club = clubRepository.findById(user.getClub().getClubId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 동아리입니다."));

        if(meetingUserRepository.existsByMeetingAndUser(meeting,user)){
            return new ResponseDto("FAIL","이미 참석한 회원입니다.");
        }

        meeting.setNumber(meeting.getNumber()+1);
        meetingRepository.save(meeting); // 모임 인원 증가

        MeetingUser meetingUser = meetingUserRepository.findByMeeting(meeting);
        meetingUser.setMeeting(meeting);
        meetingUser.setUser(user);
        meetingUserRepository.save(meetingUser);


        if(meeting.getMeetingType().equals("정기모임")){
            club.setRegularScore(club.getRegularScore()+10);
            club.setTotalScore(club.getTotalScore()+10);
            clubRepository.save(club);
            user.setRegularScore(user.getRegularScore()+10);
            user.setTotalScore(user.getTotalScore()+10);
            userRepository.save(user);
        }

        else if(meeting.getMeetingType().equals("번개모임")){
            club.setImpromptuScore(club.getImpromptuScore()+5);
            club.setTotalScore(club.getTotalScore()+5);
            clubRepository.save(club);
            user.setImpromptuScore(user.getImpromptuScore()+5);
            user.setTotalScore(user.getTotalScore()+5);
            userRepository.save(user);
        }

        else if(meeting.getMeetingType().equals("총회모임")){
            club.setOpeningScore(club.getOpeningScore()+30);
            club.setTotalScore(club.getTotalScore()+30);
            clubRepository.save(club);
            user.setOpeningScore(user.getOpeningScore()+30);
            user.setTotalScore(user.getTotalScore()+30);
            userRepository.save(user);
        }

        else{
            return new ResponseDto("FAIL","모임 유형을 다시 확인해주세요");
        }

        return new ResponseDto("SUCCESS","모임 참석 완료");
    }

    public ResponseDto getMeetingHistory(ServletRequest request) {
        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        List<MeetingUser> meetingUserList = meetingUserRepository.findAllByUser(user);

        List<SearchMeetingResponseDto> result = new ArrayList<>();

        for(MeetingUser m : meetingUserList){
            SearchMeetingResponseDto temp = new SearchMeetingResponseDto();
            temp.setMeetingId(m.getMeetingUserId());
            temp.setName(m.getMeeting().getMeetingName());
            temp.setType(m.getMeeting().getMeetingType());
            temp.setStartTime(m.getMeeting().getStartDate());
            temp.setEndTime(m.getMeeting().getEndDate());

            result.add(temp);
        }

        return new ResponseDto("SUCCESS",result);
    }
}