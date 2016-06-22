package com.pyrenty.akl.dto;

import com.pyrenty.akl.domain.enumeration.Rank;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class UserExtendedDTO {
    private Long id;
    private String login;
    private String nickname;
    private String email;
    private String firstName;
    private String lastName;
    private DateTime birthdate;
    private String guild;
    private Rank rank;
    private String description;
    private String communityId;
}
