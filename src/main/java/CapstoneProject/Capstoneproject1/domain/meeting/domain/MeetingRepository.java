package CapstoneProject.Capstoneproject1.domain.meeting.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting,Long> {
    List<Meeting> findAllByClubId(Long clubId);
}
