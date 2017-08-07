package fi.tite.akl.dto;

import fi.tite.akl.domain.Season;
import fi.tite.akl.domain.enumeration.Rank;
import lombok.Data;

@Data
public class TeamBaseDto {
    private Long id;
    private String tag;
    private String name;
    private String representing;
    private String imageUrl;
    private Rank rank;
    private String application;
    private String description;
    private Boolean activated;
    private Season season;
}
