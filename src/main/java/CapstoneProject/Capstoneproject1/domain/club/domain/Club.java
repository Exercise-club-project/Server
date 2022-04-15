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
    @Column(name = "date") // 동아리 생성 날짜
    private LocalDate date;

    @Column(name="t_score")
    private Integer totalScore;

    @Column(name="r_score")
    private Integer regularScore;

    @Column(name="i_score")
    private Integer impromptuScore;

    @Column(name="o_score")
    private Integer openingScore;

    @Builder
    public Club(String clubName, String school, Integer peopleNumber, String leader, Integer totalScore,
                Integer regularScore, Integer impromptuScore, Integer openingScore){
        this.clubName = clubName;
        this.peopleNumber = peopleNumber;
        this.school = school;
        this.leader = leader;
        this.totalScore = totalScore;
        this.regularScore = regularScore;
        this.impromptuScore = impromptuScore;
        this.openingScore = openingScore;
    }

    public void setPeopleNumber(Integer peopleNumber){
        this.peopleNumber = peopleNumber;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public void setRegularScore(Integer regularScore) {
        this.regularScore = regularScore;
    }

    public void setImpromptuScore(Integer impromptuScore) {
        this.impromptuScore = impromptuScore;
    }

    public void setOpeningScore(Integer openingScore) {
        this.openingScore = openingScore;
    }

}