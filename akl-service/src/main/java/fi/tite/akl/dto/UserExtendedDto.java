package fi.tite.akl.dto;

import fi.tite.akl.domain.enumeration.Rank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserExtendedDto {
    private Long id;
    private String login;
    private String nickname;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthdate;
    private String guild;
    private Rank rank;
    private String description;
    private String communityId;
    private List<TeamBaseDto> teams;
}
