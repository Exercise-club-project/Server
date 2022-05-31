package CapstoneProject.Capstoneproject1.domain.meeting.domain;

import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface MeetingUserRepository extends JpaRepository<MeetingUser,Long> {
    MeetingUser findByMeeting(Long meeting);
    List<MeetingUser> findAllByUser(User user);
    List<MeetingUser> findAllByMeeting(Meeting meeting1);
    /* By~는 자바 클래스에 있는 속성 값을 인자로 받아서 찾는 것, 즉 By~(인자) 인자에 속성값이 들어와야 함 */
}
