package fi.tite.akl.dto;

import fi.tite.akl.domain.enumeration.Rank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamDto {
    private Long id;
    private String tag;
    private String name;
    private String representing;
    private String imageUrl;
    private Rank rank;
    private String application;
    private String description;
    private Boolean activated;
    private UserPublicDto captain;
    private List<UserPublicDto> members = new ArrayList<>();
}
