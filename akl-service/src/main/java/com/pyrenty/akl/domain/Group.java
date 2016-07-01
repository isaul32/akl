package com.pyrenty.akl.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sauli on 5.6.2016.
 * First teams play in group against each other and two best team of group goes to the next bracket
 */
@Getter
@Setter
@Entity
@Table(name = "akl_group")
public class Group extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 50)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "akl_group_team", inverseJoinColumns = {
            @JoinColumn(name = "team_id")
    }, joinColumns = {
            @JoinColumn(name = "group_id")
    })
    private Set<Team> teams = new HashSet<>();

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    private String url;

    private String subdomain;
}
