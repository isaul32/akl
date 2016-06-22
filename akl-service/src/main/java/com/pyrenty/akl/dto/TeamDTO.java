package com.pyrenty.akl.dto;

import com.pyrenty.akl.domain.enumeration.Rank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class TeamDTO {
    private Long id;
    private String tag;
    private String name;
    private String representing;
    private String imageUrl;
    private Rank rank;
    private String description;
    private Boolean activated;
    private UserPublicDTO captain;
    private Set<UserPublicDTO> members = new HashSet<>();
    private Set<UserPublicDTO> standins = new HashSet<>();
}
