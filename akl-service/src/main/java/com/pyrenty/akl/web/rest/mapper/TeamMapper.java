package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.*;
import com.pyrenty.akl.dto.TeamDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TeamMapper {
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "standins", ignore = true)
    TeamDTO teamToTeamDTOWithoutMembers(Team team);

    TeamDTO teamToTeamDTO(Team team);

    @Mapping(target = "captain", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "standins", ignore = true)
    @Mapping(target = "requests", ignore = true)
    Team teamDTOToTeam(TeamDTO teamDTO);
}
