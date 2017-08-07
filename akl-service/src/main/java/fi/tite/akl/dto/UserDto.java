package fi.tite.akl.dto;

import fi.tite.akl.domain.enumeration.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = 1, max = 20)
    private String nickname;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private Date birthdate;

    @Size(min = 1, max = 50)
    private String guild;

    private String description;

    private Rank rank;

    private boolean activated = false;

    @Size(max = 20)
    private String communityId;

    @Size(min = 10, max = 20)
    private String steamId;

    @Size(min = 2, max = 5)
    private String langKey;

    private Set<AuthorityDto> authorities;

    private boolean requested;
}
