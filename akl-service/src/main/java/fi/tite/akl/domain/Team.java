package fi.tite.akl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.tite.akl.domain.enumeration.Rank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "akl_team")
@ToString(of = {"id", "tag", "name"})
public class Team implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "tag", length = 16, nullable = false)
    private String tag;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 100)
    @Column(name = "representing")
    private String representing;

    @Size(max = 255)
    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank")
    private Rank rank;

    @Size(max = 2048)
    @Column(name = "application")
    private String application;

    @Size(max = 2048)
    @Column(name = "description")
    private String description;

    @Column(name = "activated")
    private boolean activated;

    @NotNull
    @ManyToOne
    private User captain;

    @OrderBy("nickname ASC")
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "akl_team_user",
            joinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private List<User> members = new ArrayList<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "akl_team_request",
            joinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private List<User> requests = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    private Season season;

}
