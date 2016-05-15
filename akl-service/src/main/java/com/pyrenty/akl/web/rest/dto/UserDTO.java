package com.pyrenty.akl.web.rest.dto;

import com.pyrenty.akl.domain.enumeration.Rank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.joda.time.DateTime;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ToString
public class UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 100;

    private Long id;

    @Pattern(regexp = "^[a-z0-9]*$")
    @NotNull
    @Size(min = 1, max = 50)
    private String login;

    @NotNull
    @Size(min = 1, max = 20)
    private String nickname;

    @NotNull
    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    private DateTime birthdate;

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

    private List<String> roles;

    public UserDTO() {

    }

    public UserDTO(Long id, String login, String nickname, String password, String firstName,
                   String lastName, String email, DateTime birthdate, String guild, String description, Rank rank,
                   boolean activated, String langKey,
                   String communityId, String steamId, List<String> roles) {
        this.id = id;
        this.login = login;
        this.nickname = nickname;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.birthdate = birthdate;
        this.guild = guild;
        this.description = description;
        this.rank = rank;
        this.activated = activated;
        this.langKey = langKey;
        this.communityId = communityId;
        this.steamId = steamId;
        this.roles = roles;
    }
}
