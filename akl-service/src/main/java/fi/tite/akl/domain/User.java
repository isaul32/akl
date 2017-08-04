package fi.tite.akl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fi.tite.akl.domain.enumeration.Rank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "akl_user")
@ToString(of = {"login"})
@EqualsAndHashCode(of = {"login"}, callSuper = false)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;

    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, nullable = false)
    private String nickname;

    @NotNull
    @JsonIgnore
    @Size(min = 60, max = 60)
    @Column(length = 60)
    private String password;

    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;

    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Size(max = 100)
    @Column(length = 100, unique = true)
    private String email;

    @Column(nullable = false)
    private boolean activated = false;

    @Size(min = 2, max = 5)
    @Column(name = "lang_key", length = 5)
    private String langKey;

    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    private String resetKey;

    private Date birthdate;

    @Size(min = 1, max = 50)
    @Column
    private String guild;

    @Enumerated(EnumType.STRING)
    @Column(name = "rank")
    private Rank rank;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @Size(max = 20)
    @Column(name = "community_id", length = 20, unique = true, nullable = false)
    private String communityId;

    @Size(max = 20)
    @Column(name = "steam_id", length = 20, unique = true)
    private String steamId;

    @OneToOne
    @JsonIgnore
    private Team captain;

    @ManyToOne
    @JsonIgnore
    private Team member;

    @ManyToOne
    @JsonIgnore
    private Team standin;

    @Column(name = "reset_date")
    private LocalDateTime resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "akl_user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersistentToken> persistentTokens = new HashSet<>();

    private boolean captainSameAsFormer(Team team) {
        if (this.captain == null) {
            return team == null;
        }

        return this.captain.equals(team);
    }

}
