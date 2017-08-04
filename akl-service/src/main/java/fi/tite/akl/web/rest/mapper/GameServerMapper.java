package fi.tite.akl.web.rest.mapper;

import fi.tite.akl.domain.*;
import fi.tite.akl.dto.GameServerDto;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface GameServerMapper {
    GameServerDto gameServerToGameServerDto(GameServer gameServer);

    GameServer gameServerDtoToGameServer(GameServerDto gameServerDto);
}
