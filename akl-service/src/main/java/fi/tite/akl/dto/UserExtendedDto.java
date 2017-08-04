package fi.tite.akl.dto;

import fi.tite.akl.domain.enumeration.Rank;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

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
}
