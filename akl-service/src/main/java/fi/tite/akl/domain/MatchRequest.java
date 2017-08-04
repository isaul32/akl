package fi.tite.akl.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "akl_match_request")
public class MatchRequest {

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

    /*@NotNull
    @Column(name = "match_type")
    private Integer matchType;*/

    @NotNull
    @Column(name = "team1_accepted")
    private Boolean team1Accepted;

    @NotNull
    @Column(name = "team2_accepted")
    private Boolean team2Accepted;
}
