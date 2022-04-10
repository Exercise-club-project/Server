package CapstoneProject.Capstoneproject1.domain.meeting.domain;

import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "score")
@Getter
public class Score<Club> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "score_id")
    private Long scoreId;

    @Column(name="t_score")
    private Integer totalScore;

    @Column(name="r_score")
    private Integer regularScore;

    @Column(name="i_score")
    private Integer impromptuScore;

    @Column(name="o_score")
    private Integer openingScore;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private CapstoneProject.Capstoneproject1.domain.club.domain.Club club;

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

    public void setClub(CapstoneProject.Capstoneproject1.domain.club.domain.Club club) {
        this.club = club;
    }
}
