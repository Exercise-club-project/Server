package CapstoneProject.Capstoneproject1.domain.meeting.domain;

import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "meeting_user")
@NoArgsConstructor
@Getter
@Setter
public class MeetingUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_user_id")
    private Long meetingUserId;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
}
