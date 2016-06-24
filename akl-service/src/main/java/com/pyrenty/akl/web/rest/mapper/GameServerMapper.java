package com.pyrenty.akl.web.rest.mapper;

import com.pyrenty.akl.domain.*;
import com.pyrenty.akl.dto.GameServerDto;

import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface GameServerMapper {
    GameServerDto gameServerToGameServerDto(GameServer gameServer);

    GameServer gameServerDtoToGameServer(GameServerDto gameServerDto);
}
