package com.pyrenty.akl.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pyrenty.akl.domain.enumeration.Rank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "akl_user")
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

    @JsonIgnore
    @NotNull
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

    @Setter
    @Getter
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime birthdate;

    @Setter
    @Getter
    @Size(min = 1, max = 50)
    @Column
    private String guild;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "rank")
    private Rank rank;

    @Setter
    @Getter
    @Column(name = "description")
    private String description;

    @Size(max = 20)
    @Column(name = "community_id", length = 20, unique = true, nullable = false)
    private String communityId;

    @Size(max = 20)
    @Column(name = "steam_id", length = 20, unique = true)
    private String steamId;

    @JsonIgnore
    @OneToOne
    private Team captain;

    @JsonIgnore
    @ManyToOne
    private Team member;

    @JsonIgnore
    @ManyToOne
    private Team standin;


    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "reset_date", nullable = true)
    private DateTime resetDate = null;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Authority> authorities = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PersistentToken> persistentTokens = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public Team getCaptain() {
        return captain;
    }

    public void setCaptain(Team captain) {
        this.captain = captain;
    }

    private boolean captainSameAsFormer(Team team) {
        if (this.captain == null) {
            return team == null;
        }

        return this.captain.equals(team);
    }

    public Team getMember() {
        return member;
    }

    public void setMember(Team member) {
        this.member = member;
    }

    public Team getStandin() {
        return standin;
    }

    public void setStandin(Team standin) {
        this.standin = standin;
    }

    public DateTime getResetDate() {
       return resetDate;
    }

    public void setResetDate(DateTime resetDate) {
       this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public Set<PersistentToken> getPersistentTokens() {
        return persistentTokens;
    }

    public void setPersistentTokens(Set<PersistentToken> persistentTokens) {
        this.persistentTokens = persistentTokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (communityId == null) {
            return false;
        }
        User user = (User) o;
        if (user.communityId == null) {
            return false;
        }

        return communityId.equals(user.communityId);
    }

    @Override
    public int hashCode() {
        return communityId == null ? 0 : communityId.hashCode();
    }

    @Override
    public String toString() {
        return "User{" + "login='" + login + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                "}";
    }
}
