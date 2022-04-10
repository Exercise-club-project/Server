package CapstoneProject.Capstoneproject1.domain.meeting.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "meeting")
@Getter
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_id")
    private Long meetingId;

    @Column(name = "meeting_name")
    private String meetingName;

    @Column(name = "meeting_type")
    private String meetingType;

    @Column(name = "start_date")
    private String startDate;

    @Column(name = "end_date")
    private String endDate;

    @Column(name = "number") // 참여인원
    private Integer number;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "club_id")
    private Long clubId;

    @Builder
    public Meeting(String meetingName, String meetingType, String startDate, String endDate, Integer number,
                   String description, Long clubId){
        this.meetingName = meetingName;
        this.meetingType = meetingType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.number = number;
        this.description = description;
        this.clubId = clubId;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}