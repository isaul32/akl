package fi.tite.akl.dto;

import lombok.Data;

@Data
public class UserTransferDto {
    private String nickname;
    private String firstName;
    private String lastName;
    private String email;
    private String guild;
    private String communityId;
    private String steamId;
    private Long captainId;
    private Long memberId;
    private Long standinId;
}
