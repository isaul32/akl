package fi.tite.akl.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserTransferDto {
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private Date birthdate;
    private String guild;
    private String communityId;
    private String steamId;
    private Long captain;
    private Long member;
}
