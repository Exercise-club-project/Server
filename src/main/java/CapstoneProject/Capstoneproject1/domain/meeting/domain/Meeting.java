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

    @Builder
    public Meeting(String meetingType, String startDate, String endDate, Integer number, String description){
        this.meetingType = meetingType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.number = number;
        this.description = description;
    }

}