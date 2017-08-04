package fi.tite.akl.web.rest;

import fi.tite.akl.dto.GameServerDto;
import fi.tite.akl.service.GameServerService;
import fi.tite.akl.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GameServer.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class GameServerResource {

    @Inject
    private GameServerService gameServerService;

    @RequestMapping(value = "/gameServers",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameServerDto> createGameServer(@RequestBody GameServerDto gameServerDto) throws URISyntaxException {
        log.debug("REST request to save GameServer : {}", gameServerDto);
        if (gameServerDto.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("gameServer", "idexists", "A new gameServer cannot already have an ID")).body(null);
        }
        GameServerDto result = gameServerService.save(gameServerDto);
        return ResponseEntity.created(new URI("/api/gameServers/" + result.getId()))
                .headers(HeaderUtil.createAlert("gameServer", result.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/gameServers",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameServerDto> updateGameServer(@RequestBody GameServerDto gameServerDto) throws URISyntaxException {
        log.debug("REST request to update GameServer : {}", gameServerDto);
        if (gameServerDto.getId() == null) {
            return createGameServer(gameServerDto);
        }
        GameServerDto result = gameServerService.save(gameServerDto);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("gameServer", gameServerDto.getId().toString()))
                .body(result);
    }

    @RequestMapping(value = "/gameServers",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GameServerDto> getAllGameServers() {
        log.debug("REST request to get all GameServers");
        return gameServerService.findAll();
    }

    @RequestMapping(value = "/gameServers/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameServerDto> getGameServer(@PathVariable Long id) {
        log.debug("REST request to get GameServer : {}", id);
        GameServerDto gameServerDto = gameServerService.findOne(id);
        return Optional.ofNullable(gameServerDto)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/gameServers/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteGameServer(@PathVariable Long id) {
        log.debug("REST request to delete GameServer : {}", id);
        gameServerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("gameServer", id.toString())).build();
    }

}
