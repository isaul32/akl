package com.pyrenty.akl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pyrenty.akl.domain.enumeration.Rank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "akl_team")
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

    @Column(name = "description")
    private String description;

    @Column(name = "activated")
    private boolean activated;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "captain")
    private User captain;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "member")
    private Set<User> members = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "standin")
    private Set<User> standins = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinTable(
            name = "akl_team_request",
            joinColumns = {@JoinColumn(name = "team_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<User> requests = new HashSet<>();

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", tag='" + tag + "'" +
                ", name='" + name + "'" +
                '}';
    }
}
