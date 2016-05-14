package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Team;
import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.service.TeamService;
import com.pyrenty.akl.service.UserService;
import com.pyrenty.akl.web.rest.errors.CustomParameterizedException;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import com.pyrenty.akl.web.rest.dto.TeamDTO;
import com.pyrenty.akl.web.rest.mapper.TeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * REST controller for managing Team.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {
    private final Logger log = LoggerFactory.getLogger(TeamResource.class);

    @Inject
    private UserService userService;

    @Inject
    private TeamService teamService;

    @Inject
    private TeamMapper teamMapper;

    @RequestMapping(value = "/teams", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<TeamDTO> create(@Valid @RequestBody TeamDTO teamDTO) throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDTO);

        User user = userService.getUserWithAuthorities();

        if (user.getEmail() == null) {
            throw new CustomParameterizedException("Can't create a team without an email set");
        } else if (teamDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new team cannot already have an ID").body(null);
        }

        Team team = teamService.create(teamMapper.teamDTOToTeam(teamDTO));

        return ResponseEntity.created(new URI("/api/teams/" + team.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("team", team.getId().toString()))
                .body(teamMapper.teamToTeamDTO(team));
    }

    @RequestMapping(value = "/teams", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<TeamDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                                @RequestParam(value = "per_page", required = false) Integer limit,
                                                @RequestParam(required = false) String filter) throws URISyntaxException {
        log.debug("REST request to get all Teams");

        Page<Team> page = teamService.getAll(PaginationUtil.generatePageRequest(offset, limit));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/teams", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(teamMapper::teamToTeamDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/teams/{id}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<TeamDTO> get(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);

        return teamService.get(id)
            .map(teamMapper::teamToTeamDTO)
            .map(teamDTO -> new ResponseEntity<>(teamDTO, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/teams/{id}", method = RequestMethod.PUT)
    @Timed
    public ResponseEntity<TeamDTO> put(@PathVariable Long id,
                                       @Valid @RequestBody TeamDTO bodyDTO) {
        log.debug("REST request to put Team : {}", id);

        return Optional.ofNullable(teamService.update(teamMapper.teamDTOToTeam(bodyDTO)))
                .map(teamMapper::teamToTeamDTO)
                .map(teamDTO -> new ResponseEntity<>(teamDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/teams/{id}/activate", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        return Optional.ofNullable(teamService.activate(id))
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/teams/{id}",
            method = RequestMethod.DELETE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);

        teamService.delete(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("team", id.toString())).build();
    }

}
