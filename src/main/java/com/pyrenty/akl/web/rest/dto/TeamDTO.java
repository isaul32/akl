package com.pyrenty.akl.web.rest.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

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

    @Size(max = 255)
    private String imageUrl;

    private Rank rank;

    private String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TeamDTO teamDTO = (TeamDTO) o;

        if ( ! Objects.equals(id, teamDTO.id)) return false;

        return true;
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
