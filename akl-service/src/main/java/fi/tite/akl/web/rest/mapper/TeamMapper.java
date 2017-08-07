package fi.tite.akl.web.rest.mapper;

import fi.tite.akl.domain.*;
import fi.tite.akl.dto.TeamDto;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface TeamMapper {
    @Mapping(target = "captain", ignore = true)
    @Mapping(target = "members", ignore = true)
    TeamDto teamToTeamDtoWithoutMembers(Team team);

    TeamDto teamToTeamDto(Team team);

    @Mapping(target = "requests", ignore = true)
    @Mapping(target = "season", ignore = true)
    Team teamDtoToTeam(TeamDto teamDto);
}
