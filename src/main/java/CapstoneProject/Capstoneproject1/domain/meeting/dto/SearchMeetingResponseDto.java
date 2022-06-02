package CapstoneProject.Capstoneproject1.domain.meeting.dto;

import lombok.Data;

@Data
public class SearchMeetingResponseDto {

    Long meetingId;
    String name;
    String type;
    String startTime;
    String endTime;

}
