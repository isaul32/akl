package fi.tite.akl.dto;

import fi.tite.akl.domain.enumeration.Rank;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String login;
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthdate;
    private String guild;
    private String description;
    private Rank rank;
    private boolean activated = false;
    private String communityId;
    private String steamId;
    private String langKey;
    private Set<AuthorityDto> authorities;
    private List<TeamBaseDto> teams;
}
