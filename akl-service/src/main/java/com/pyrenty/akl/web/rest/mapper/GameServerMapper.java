package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.*;
import com.pyrenty.akl.dto.GameServerDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface GameServerMapper {
    GameServerDTO gameServerToGameServerDTO(GameServer gameServer);

    GameServer gameServerDTOToGameServer(GameServerDTO gameServerDTO);
}
