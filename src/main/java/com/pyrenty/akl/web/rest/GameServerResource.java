package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.service.GameServerService;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.dto.GameServerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GameServer.
 */
@RestController
@RequestMapping("/api")
public class GameServerResource {

    private final Logger log = LoggerFactory.getLogger(GameServerResource.class);

    @Inject
    private GameServerService gameServerService;


    /**
     * POST  /gameServers -> Create a new gameServer.
     */
    @RequestMapping(value = "/gameServers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GameServerDTO> createGameServer(@RequestBody GameServerDTO gameServerDTO) throws URISyntaxException {
        log.debug("REST request to save GameServer : {}", gameServerDTO);
        if (gameServerDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("gameServer", "idexists", "A new gameServer cannot already have an ID")).body(null);
        }
        GameServerDTO result = gameServerService.save(gameServerDTO);
        return ResponseEntity.created(new URI("/api/gameServers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("gameServer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /gameServers -> Updates an existing gameServer.
     */
    @RequestMapping(value = "/gameServers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GameServerDTO> updateGameServer(@RequestBody GameServerDTO gameServerDTO) throws URISyntaxException {
        log.debug("REST request to update GameServer : {}", gameServerDTO);
        if (gameServerDTO.getId() == null) {
            return createGameServer(gameServerDTO);
        }
        GameServerDTO result = gameServerService.save(gameServerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("gameServer", gameServerDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /gameServers -> get all the gameServers.
     */
    @RequestMapping(value = "/gameServers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public List<GameServerDTO> getAllGameServers() {
        log.debug("REST request to get all GameServers");
        return gameServerService.findAll();
            }

    /**
     * GET  /gameServers/:id -> get the "id" gameServer.
     */
    @RequestMapping(value = "/gameServers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<GameServerDTO> getGameServer(@PathVariable Long id) {
        log.debug("REST request to get GameServer : {}", id);
        GameServerDTO gameServerDTO = gameServerService.findOne(id);
        return Optional.ofNullable(gameServerDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /gameServers/:id -> delete the "id" gameServer.
     */
    @RequestMapping(value = "/gameServers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGameServer(@PathVariable Long id) {
        log.debug("REST request to delete GameServer : {}", id);
        gameServerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("gameServer", id.toString())).build();
    }

}
