package com.pyrenty.akl.web.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Team Request managenet (accepting and managing team members)
 */
@Data
@NoArgsConstructor
public class TeamRequestDTO {
    private Long member;
    private String role;
    private Long swapMember;
}
