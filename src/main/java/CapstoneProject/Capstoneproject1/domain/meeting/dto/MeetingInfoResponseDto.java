package CapstoneProject.Capstoneproject1.domain.meeting.dto;

import lombok.Data;
import java.util.List;

@Data
public class MeetingInfoResponseDto {
    String meetingName;
    String meetingType;
    String startDate;
    String endDate;
    String description;
    List<String> joinList;
}
