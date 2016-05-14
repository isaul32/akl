package com.pyrenty.akl.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.pyrenty.akl.domain.user.User;
import com.pyrenty.akl.domain.enumeration.Rank;

/**
 * A DTO for the Team entity.
 */
public class TeamDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 16)
    private String tag;

    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    private String representing;

    @Size(max = 255)
    private String imageUrl;

    private Rank rank;

    private String description;

    private Boolean activated;

    private User captain;

    private Set<User> members = new HashSet<>();

    private Set<User> standins = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRepresenting() {
        return representing;
    }

    public void setRepresenting(String representing) {
        this.representing = representing;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActivated() {
        return this.activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public User getCaptain() {
        return captain;
    }

    public void setCaptain(User captain) {
        this.captain = captain;
    }

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<User> getStandins() {
        return standins;
    }

    public void setStandins(Set<User> standins) {
        this.standins = standins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeamDTO teamDTO = (TeamDTO) o;

        return Objects.equals(id, teamDTO.id);

    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TeamDTO{" +
                "id=" + id +
                ", tag='" + tag + "'" +
                ", name='" + name + "'" +
                ", imageUrl='" + imageUrl + "'" +
                ", rank='" + rank + "'" +
                ", description='" + description + "'" +
                '}';
    }
}
