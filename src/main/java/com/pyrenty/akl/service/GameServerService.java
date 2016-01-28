package com.pyrenty.akl.service;

import com.pyrenty.akl.domain.GameServer;
import com.pyrenty.akl.repository.GameServerRepository;
import com.pyrenty.akl.web.rest.dto.GameServerDTO;
import com.pyrenty.akl.web.rest.mapper.GameServerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing GameServer.
 */
@Service
@Transactional
public class GameServerService {

    private final Logger log = LoggerFactory.getLogger(GameServerService.class);

    @Inject
    private GameServerRepository gameServerRepository;

    @Inject
    private GameServerMapper gameServerMapper;

    /**
     * Save a gameServer.
     * @return the persisted entity
     */
    public GameServerDTO save(GameServerDTO gameServerDTO) {
        log.debug("Request to save GameServer : {}", gameServerDTO);
        GameServer gameServer = gameServerMapper.gameServerDTOToGameServer(gameServerDTO);
        gameServer = gameServerRepository.save(gameServer);
        return gameServerMapper.gameServerToGameServerDTO(gameServer);
    }

    /**
     *  get all the gameServers.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<GameServerDTO> findAll() {
        log.debug("Request to get all GameServers");
        return gameServerRepository.findAll().stream()
            .map(gameServerMapper::gameServerToGameServerDTO)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  get one gameServer by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public GameServerDTO findOne(Long id) {
        log.debug("Request to get GameServer : {}", id);
        GameServer gameServer = gameServerRepository.findOne(id);
        return gameServerMapper.gameServerToGameServerDTO(gameServer);
    }

    /**
     *  delete the  gameServer by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete GameServer : {}", id);
        gameServerRepository.delete(id);
    }

}
