package CapstoneProject.Capstoneproject1.domain.club.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "club")
@NoArgsConstructor
@Getter
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

    @Column(name = "date")
    private String date;

    @Column(name = "ranking") // "rank"는 데이터베이스에서 이미 사용하는 예약어이기 때문에 ranking으로 바꿔줌
    private Integer rank;

    @Builder
    public Club(String clubName, String school, Integer peopleNumber, String leader, String date, Integer rank){
        this.clubName = clubName;
        this.school = school;
        this.peopleNumber = peopleNumber;
        this.leader = leader;
        this.date = date;
        this.rank = rank;
    }

}