package CapstoneProject.Capstoneproject1.domain.meeting.domain;

import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score,Long> {
    Score findByClub(Club club);
}
