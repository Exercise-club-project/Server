package CapstoneProject.Capstoneproject1.domain.meeting.dto;

import lombok.Data;

@Data
public class CreateMeetingRequestDto {
    Long meetingId;
    String meetingName;
    String meetingType;
    String startDate;
    String endDate;
    String description;
}
