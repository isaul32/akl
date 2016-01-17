package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.*;
import com.pyrenty.akl.web.rest.dto.GameServerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GameServer and its DTO GameServerDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GameServerMapper {

    GameServerDTO gameServerToGameServerDTO(GameServer gameServer);

    GameServer gameServerDTOToGameServer(GameServerDTO gameServerDTO);
}
