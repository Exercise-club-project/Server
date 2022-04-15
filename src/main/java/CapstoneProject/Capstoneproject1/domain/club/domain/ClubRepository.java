package CapstoneProject.Capstoneproject1.domain.club.domain;

import CapstoneProject.Capstoneproject1.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Long> {
    boolean existsByClubName(String clubName);

    @Query(value = "SELECT c FROM Club c WHERE c.school LIKE %:school%")
    List<Club> findAllBySchool(@Param("school") String school);
}
