package CapstoneProject.Capstoneproject1.domain.meeting.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.Meeting;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.MeetingRepository;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.MeetingUser;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.MeetingUserRepository;
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

        CreateMeetingRequestDto result = CreateMeetingRequestDto.builder()
                .meetingName(meeting.getMeetingName())
                .meetingType(meeting.getMeetingType())
                .startDate(meeting.getStartDate())
                .endDate(meeting.getEndDate())
                .description(meeting.getDescription())
                .build();

        return new ResponseDto("SUCCESS",result);

    }
}