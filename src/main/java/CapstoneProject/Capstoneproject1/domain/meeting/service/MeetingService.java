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

        return new ResponseDto("SUCCESS",meeting.getMeetingId()); // ????????? "meeting"??? id??? ??????
    }

    public ResponseDto searchMeeting(Long groupId, ServletRequest request){

        List<Meeting> meetingList =  meetingRepository.findAllByClubId(groupId); // ???????????? ????????? ?????? ??????

        LocalDateTime now = LocalDateTime.now(); // ?????? ??????

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); // "dateTime" ??????

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
                temp.setEndTime(m.getEndDate());

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
                temp.add(m.getUser().getName());
        }
            result.setJoinList(temp);
        }

        catch (NullPointerException e){
            temp.add("????????? ????????? ???????????? ????????????.");
            result.setJoinList(temp);
        }

        return new ResponseDto("SUCCESS",result);

    }

    public ResponseDto joinMeeting(Long userId, Long meetingId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ???????????????."));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(()->new IllegalArgumentException("???????????? ?????? ???????????????."));

        Club club = clubRepository.findById(user.getClub().getClubId())
                .orElseThrow(() -> new IllegalArgumentException("???????????? ?????? ??????????????????."));

        if(meetingUserRepository.existsByMeetingAndUser(meeting,user)){
            return new ResponseDto("FAIL","?????? ????????? ???????????????.");
        }

        meeting.setNumber(meeting.getNumber()+1);
        meetingRepository.save(meeting); // ?????? ?????? ??????

        // MeetingUser meetingUser = meetingUserRepository.findByMeeting(meeting);: 6??? 8??? ?????? 7:49 ?????? ????????????
        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setMeeting(meeting);
        meetingUser.setUser(user);
        meetingUserRepository.save(meetingUser);


        if(meeting.getMeetingType().equals("????????????")){
            club.setRegularScore(club.getRegularScore()+10);
            club.setTotalScore(club.getTotalScore()+10);
            clubRepository.save(club);
            user.setRegularScore(user.getRegularScore()+10);
            user.setTotalScore(user.getTotalScore()+10);
            userRepository.save(user);
        }

        else if(meeting.getMeetingType().equals("????????????")){
            club.setImpromptuScore(club.getImpromptuScore()+5);
            club.setTotalScore(club.getTotalScore()+5);
            clubRepository.save(club);
            user.setImpromptuScore(user.getImpromptuScore()+5);
            user.setTotalScore(user.getTotalScore()+5);
            userRepository.save(user);
        }

        else if(meeting.getMeetingType().equals("????????????")){
            club.setOpeningScore(club.getOpeningScore()+30);
            club.setTotalScore(club.getTotalScore()+30);
            clubRepository.save(club);
            user.setOpeningScore(user.getOpeningScore()+30);
            user.setTotalScore(user.getTotalScore()+30);
            userRepository.save(user);
        }

        else{
            return new ResponseDto("FAIL","?????? ????????? ?????? ??????????????????");
        }

        return new ResponseDto("SUCCESS","?????? ?????? ??????");
    }

    public ResponseDto getMeetingHistory(ServletRequest request) {
        String token = jwtAuthenticationProvider.resolveToken((HttpServletRequest) request);
        User user = userRepository.findByEmail(jwtAuthenticationProvider.getUserPk(token));

        List<MeetingUser> meetingUserList = meetingUserRepository.findAllByUser(user);

        List<SearchMeetingResponseDto> result = new ArrayList<>();

        for(MeetingUser m : meetingUserList){
            SearchMeetingResponseDto temp = new SearchMeetingResponseDto();
            temp.setMeetingId(m.getMeeting().getMeetingId());
            temp.setName(m.getMeeting().getMeetingName());
            temp.setType(m.getMeeting().getMeetingType());
            temp.setStartTime(m.getMeeting().getStartDate());
            temp.setEndTime(m.getMeeting().getEndDate());

            result.add(temp);
        }

        return new ResponseDto("SUCCESS",result);
    }
}