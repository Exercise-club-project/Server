package CapstoneProject.Capstoneproject1.domain.meeting.controller;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.meeting.dto.CreateMeetingRequestDto;
import CapstoneProject.Capstoneproject1.domain.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;

@Controller
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @PostMapping("/user/meeting/create")
    @ResponseBody
    public ResponseDto createMeeting(@RequestBody CreateMeetingRequestDto createMeetingRequestDto, ServletRequest request){
       return meetingService.createMeeting(createMeetingRequestDto, request);
    }


}
