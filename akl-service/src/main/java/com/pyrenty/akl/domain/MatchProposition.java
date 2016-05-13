package com.pyrenty.akl.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Sauli on 9.4.2016.
 */
@Entity
@Table(name = "match_proposition")
public class MatchProposition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team1;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team2;

    @NotNull
    private Date datetime;

    @NotNull
    @Column(name = "match_type")
    private Integer matchType;

    @NotNull
    @Column(name = "team1_accepted")
    private Boolean team1Accepted;

    @NotNull
    @Column(name = "team2_accepted")
    private Boolean team2Accepted;

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Integer getMatchType() {
        return matchType;
    }

    public void setMatchType(Integer matchType) {
        this.matchType = matchType;
    }

    public Boolean getTeam1Accepted() {
        return team1Accepted;
    }

    public void setTeam1Accepted(Boolean team1Accepted) {
        this.team1Accepted = team1Accepted;
    }

    public Boolean getTeam2Accepted() {
        return team2Accepted;
    }

    public void setTeam2Accepted(Boolean team2Accepted) {
        this.team2Accepted = team2Accepted;
    }
}
