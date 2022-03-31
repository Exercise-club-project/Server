package CapstoneProject.Capstoneproject1.domain.club.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club,Long> {
    boolean existsByClubName(String clubName);
}
