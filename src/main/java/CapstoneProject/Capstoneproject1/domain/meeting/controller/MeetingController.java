package CapstoneProject.Capstoneproject1.domain.meeting.controller;

import CapstoneProject.Capstoneproject1.domain.ResponseDto;
import CapstoneProject.Capstoneproject1.domain.meeting.dto.CreateMeetingRequestDto;
import CapstoneProject.Capstoneproject1.domain.meeting.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Servlet;
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

    @GetMapping("/user/meeting/get/{groupId}")
    @ResponseBody
    public ResponseDto searchMeeting(@PathVariable Long groupId, ServletRequest request){
        return meetingService.searchMeeting(groupId,request);
    }

    @GetMapping("/user/meetingInfo/get/{meetingId}")
    @ResponseBody
    public ResponseDto getMeetingInfo(@PathVariable Long meetingId){
        return meetingService.getMeetingInfo(meetingId);
    }

    @PostMapping("/user/meeting/join/{meetingId}")
    @ResponseBody
    public ResponseDto joinMeeting(@PathVariable Long meetingId, ServletRequest request){
        return meetingService.joinMeeting(meetingId, request);
    }

    @GetMapping("/user/meeting/history")
    @ResponseBody
    public ResponseDto getMeetingHistory(ServletRequest request){
        return meetingService.getMeetingHistory(request);
    }
}
