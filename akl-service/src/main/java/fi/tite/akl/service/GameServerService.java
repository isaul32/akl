package fi.tite.akl.service;

import fi.tite.akl.domain.GameServer;
import fi.tite.akl.dto.GameServerDto;
import fi.tite.akl.repository.GameServerRepository;
import fi.tite.akl.mapper.GameServerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing GameServer.
 */
@Slf4j
@Service
@Transactional
public class GameServerService {

    @Inject
    private GameServerRepository gameServerRepository;

    @Inject
    private GameServerMapper gameServerMapper;

    /**
     * Save a gameServer.
     * @return the persisted entity
     */
    public GameServerDto save(GameServerDto gameServerDto) {
        log.debug("Request to save GameServer : {}", gameServerDto);
        GameServer gameServer = gameServerMapper.gameServerDtoToGameServer(gameServerDto);
        gameServer = gameServerRepository.save(gameServer);
        return gameServerMapper.gameServerToGameServerDto(gameServer);
    }

    /**
     *  get all the gameServers.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<GameServerDto> findAll() {
        log.debug("Request to get all GameServers");
        return gameServerRepository.findAll().stream()
            .map(gameServerMapper::gameServerToGameServerDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  get one gameServer by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public GameServerDto findOne(Long id) {
        log.debug("Request to get GameServer : {}", id);
        GameServer gameServer = gameServerRepository.findOne(id);
        return gameServerMapper.gameServerToGameServerDto(gameServer);
    }

    /**
     *  delete the  gameServer by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete GameServer : {}", id);
        gameServerRepository.delete(id);
    }
}
