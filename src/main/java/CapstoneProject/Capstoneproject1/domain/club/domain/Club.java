package CapstoneProject.Capstoneproject1.domain.club.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "club")
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long clubId;

    @Column(name = "name")
    private String clubName;

    @Column(name = "school")
    private String school;

    @Column(name = "people_number")
    private Integer peopleNumber;

    @Column(name = "leader")
    private String leader;

    @CreatedDate
    @Column(name = "date")
    private LocalDate date;

    @Column(name = "ranking") // "rank"는 데이터베이스에서 이미 사용하는 예약어이기 때문에 "ranking"으로 바꿔줌
    private Integer rank;

    @Builder
    public Club(String clubName, String school, String leader){
        this.clubName = clubName;
        this.school = school;
        this.leader = leader;
    }
}