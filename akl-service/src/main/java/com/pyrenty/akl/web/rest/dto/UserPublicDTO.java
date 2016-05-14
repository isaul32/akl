package com.pyrenty.akl.web.rest.dto;

import com.pyrenty.akl.domain.enumeration.Rank;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class UserPublicDTO {
    private Long id;
    private String nickname;
    private String firstName;
    private String lastName;
    private DateTime birthdate;
    private String guild;
    private Rank rank;
    private String description;
    private String communityId;
}
