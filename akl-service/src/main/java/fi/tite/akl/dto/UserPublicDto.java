package fi.tite.akl.dto;

import fi.tite.akl.domain.enumeration.Rank;
import lombok.Data;

@Data
public class UserPublicDto {
    private Long id;
    private String nickname;
    private String firstName;
    private String lastName;
    private Long age;
    private String guild;
    private Rank rank;
    private String description;
    private String communityId;
}