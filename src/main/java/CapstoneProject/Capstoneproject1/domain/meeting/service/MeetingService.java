package CapstoneProject.Capstoneproject1.domain.meeting.service;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.config.JwtAuthenticationProvider;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.Meeting;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.MeetingRepository;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.MeetingUser;
import CapstoneProject.Capstoneproject1.domain.meeting.domain.MeetingUserRepository;
import CapstoneProject.Capstoneproject1.domain.meeting.dto.CreateMeetingRequestDto;
import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import CapstoneProject.Capstoneproject1.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

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
                .meetingType(createMeetingRequestDto.getMeetingType())
                .startDate(createMeetingRequestDto.getStartDate())
                .endDate(createMeetingRequestDto.getEndDate())
                .number(1)
                .description(createMeetingRequestDto.getDescription())
                .build();

        meetingRepository.save(meeting);

        MeetingUser meetingUser = new MeetingUser();
        meetingUser.setMeeting(meeting);
        meetingUser.setUser(user);

        meetingUserRepository.save(meetingUser);

        return new ResponseDto("SUCCESS",meeting.getMeetingId()); // 생성된 "meeting"의 id값 반환
    }
}