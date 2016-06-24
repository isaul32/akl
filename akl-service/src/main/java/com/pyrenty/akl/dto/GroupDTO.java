package com.pyrenty.akl.dto;

import com.pyrenty.akl.domain.Team;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class GroupDto {
    private Long id;
    private String name;
    private Set<Team> teams = new HashSet<>();
    private String url;
    private String subdomain;
}
