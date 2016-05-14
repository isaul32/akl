package com.pyrenty.akl.web.rest.dto;

import java.util.HashSet;
import java.util.Set;
import com.pyrenty.akl.domain.enumeration.Rank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TeamDTO {
    private Long id;
    private String tag;
    private String name;

    private String representing;

    @Size(max = 255)
    private String imageUrl;
    private Rank rank;
    private String description;
    private Boolean activated;
    private UserPublicDTO captain;
    private Set<UserPublicDTO> members = new HashSet<>();
    private Set<UserPublicDTO> standins = new HashSet<>();
}
