package com.pyrenty.akl.dto;

import com.pyrenty.akl.domain.enumeration.Rank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserExtendedDto {
    private Long id;
    private String login;
    private String nickname;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private String guild;
    private Rank rank;
    private String description;
    private String communityId;
}
