package com.pyrenty.akl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TeamRequestDto {
    private Long member;
    private String role;
    private Long swapMember;
}
