package com.pyrenty.akl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamRequestDTO {
    private Long member;
    private String role;
    private Long swapMember;
}
