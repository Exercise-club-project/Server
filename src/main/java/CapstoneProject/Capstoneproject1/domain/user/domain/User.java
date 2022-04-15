package CapstoneProject.Capstoneproject1.domain.user.domain;

import CapstoneProject.Capstoneproject1.domain.club.domain.Club;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "sex")
    private String sex;

    @Column(name = "phone_number")
    private String phoneNumber;

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
    private Club club;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Builder
    public User(String email, String password, String name, String birthDate, String sex, String phoneNumber,
                List<String> roles, Integer totalScore, Integer regularScore, Integer impromptuScore,
                Integer openingScore){
        this.email = email;
        this.name = name;
        this.birthDate = birthDate;
        this.password = password;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.roles = roles;
        this.totalScore = totalScore;
        this.regularScore = regularScore;
        this.impromptuScore = impromptuScore;
        this.openingScore = openingScore;
    }

    public void setClub(Club club){
        this.club = club;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
               .map(SimpleGrantedAuthority::new)
               .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword(){ return password;}

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
