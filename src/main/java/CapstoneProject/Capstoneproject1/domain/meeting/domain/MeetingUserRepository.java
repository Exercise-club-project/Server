package CapstoneProject.Capstoneproject1.domain.meeting.domain;

import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface MeetingUserRepository extends JpaRepository<MeetingUser,Long> {
    MeetingUser findByMeeting(Long meeting);
    List<MeetingUser> findAllByUser(User user);
    List<User> findAllByMeeting(Long meetingId);
}
