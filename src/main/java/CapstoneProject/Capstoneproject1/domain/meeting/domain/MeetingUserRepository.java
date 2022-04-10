package CapstoneProject.Capstoneproject1.domain.meeting.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingUserRepository extends JpaRepository<MeetingUser,Long> {
    MeetingUser findByMeeting(Long meeting);
}
