package CapstoneProject.Capstoneproject1.domain.meeting.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CreateMeetingRequestDto {
    String meetingName;
    String meetingType;
    String startDate;
    String endDate;
    String description;

    @Builder
    public CreateMeetingRequestDto(String meetingName, String meetingType, String startDate, String endDate, String description){
        this.meetingName = meetingName;
        this.meetingType = meetingType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }
}
