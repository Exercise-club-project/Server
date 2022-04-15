package CapstoneProject.Capstoneproject1.domain.user.domain;

import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findAllByClubOrderByTotalScoreDesc(Club club);
}
