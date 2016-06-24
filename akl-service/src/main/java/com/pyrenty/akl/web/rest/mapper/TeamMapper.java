package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.*;
import com.pyrenty.akl.dto.TeamDto;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TeamMapper {
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "standins", ignore = true)
    TeamDto teamToTeamDtoWithoutMembers(Team team);

    TeamDto teamToTeamDto(Team team);

    @Mapping(target = "captain", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "standins", ignore = true)
    @Mapping(target = "requests", ignore = true)
    Team teamDtoToTeam(TeamDto teamDto);
}
