package fi.tite.akl.dto;

import fi.tite.akl.domain.enumeration.MembershipRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamRequestDto {
    private Long member;
    private MembershipRole role;
    private Long swapMember;
}
