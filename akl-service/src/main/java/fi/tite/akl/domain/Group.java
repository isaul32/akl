package fi.tite.akl.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
