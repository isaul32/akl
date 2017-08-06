package fi.tite.akl.web.rest.mapper;

import fi.tite.akl.domain.*;
import fi.tite.akl.dto.TeamDto;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TeamMapper {
    @Mapping(target = "captain", ignore = true)
    @Mapping(target = "members", ignore = true)
    /*@Mapping(target = "standins", ignore = true)*/
    TeamDto teamToTeamDtoWithoutMembers(Team team);

    @Mapping(target = "captain", ignore = true)
    @Mapping(target = "members", ignore = true)
    TeamDto teamToTeamDto(Team team);

    @Mapping(target = "captain", ignore = true)
    @Mapping(target = "members", ignore = true)
    /*@Mapping(target = "standins", ignore = true)*/
    @Mapping(target = "requests", ignore = true)
    Team teamDtoToTeam(TeamDto teamDto);
}