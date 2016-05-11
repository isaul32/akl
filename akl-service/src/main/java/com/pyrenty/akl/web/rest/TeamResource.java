package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Team;
import com.pyrenty.akl.domain.user.User;
import com.pyrenty.akl.repository.TeamRepository;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.service.UserService;
import com.pyrenty.akl.web.rest.errors.CustomParameterizedException;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import com.pyrenty.akl.web.rest.dto.TeamDTO;
import com.pyrenty.akl.web.rest.mapper.TeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


/**
 * REST controller for managing Team.
 */
@RestController
@RequestMapping("/api")
public class TeamResource {

    private final Logger log = LoggerFactory.getLogger(TeamResource.class);

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TeamMapper teamMapper;

    /**
     * POST  /teams -> Create a new team.
     */
    @RequestMapping(value = "/teams",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Transactional
    @Timed
    public ResponseEntity<TeamDTO> create(@Valid @RequestBody TeamDTO teamDTO) throws URISyntaxException {
        User user = userService.getUserWithAuthorities();

        if (user.getEmail() == null) {
            throw new CustomParameterizedException("Can't create a team without an email set");
        }

        log.debug("REST request to save Team : {}", teamDTO);
        if (teamDTO.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new team cannot already have an ID").body(null);
        }
        Team team = teamMapper.teamDTOToTeam(teamDTO);
        team.setActivated(false);
        Team result = teamRepository.save(team);
        User currentUser = userService.getUserWithAuthorities();
        currentUser.setCaptain(result);
        userRepository.save(currentUser);
        return ResponseEntity.created(new URI("/api/teams/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("team", result.getId().toString()))
                .body(teamMapper.teamToTeamDTO(result));
    }

    /**
     * GET  /teams -> get all the teams.
     */
    @RequestMapping(value = "/teams",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<TeamDTO>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                                @RequestParam(value = "per_page", required = false) Integer limit,
                                                @RequestParam(required = false) String filter,
                                                HttpServletRequest request)
        throws URISyntaxException {
        if ("captain-is-null".equals(filter)) {
            log.debug("REST request to get all Teams where captain is null");
            return new ResponseEntity<>(StreamSupport
                .stream(teamRepository.findAll().spliterator(), false)
                .filter(team -> team.getCaptain() == null)
                .map(team -> teamMapper.teamToTeamDTO(team))
                .collect(Collectors.toList()), HttpStatus.OK);
        }

        Page<Team> page;
        Pageable paging = PaginationUtil.generatePageRequest(offset, limit);

        if (request.isUserInRole("ROLE_ADMIN")) {
            page = teamRepository.findAll(paging);
        } else {
            page = teamRepository.findByActivated(true, paging);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/teams", offset, limit);
        return new ResponseEntity<>(page.getContent().stream()
            .map(teamMapper::teamToTeamDTO)
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    /**
     * GET  /teams/:id -> get the "id" team.
     */
    @RequestMapping(value = "/teams/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TeamDTO> get(@PathVariable Long id,
                                       HttpServletRequest request) {
        log.debug("REST request to get Team : {}", id);

        Optional<Team> optional;

        if (request.isUserInRole("ROLE_ADMIN")) {
            optional = Optional.ofNullable(teamRepository.findOne(id));
        } else {
            optional = Optional.ofNullable(teamRepository.findOneByActivated(id, true));
        }

        return optional
            .map(teamMapper::teamToTeamDTO)
            .map(teamDTO -> new ResponseEntity<>(
                teamDTO,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * PUT  /teams/:id -> update the "id" team.
     */
    @RequestMapping(value = "/teams/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamDTO> put(@PathVariable Long id,
                                       @Valid @RequestBody TeamDTO teamBody,
                                       HttpServletRequest request) {
        User user = userService.getUserWithAuthorities();

        if (!request.isUserInRole("ROLE_ADMIN") && !user.getCaptain().getId().equals(id)) {
            throw new AccessDeniedException("You are not allowed to edit this team");
        }

        log.debug("REST request to put Team : {}", id);

        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    team.setTag(teamBody.getTag());
                    team.setName(teamBody.getName());
                    team.setRank(teamBody.getRank());
                    team.setDescription(teamBody.getDescription());
                    team.setImageUrl(teamBody.getImageUrl());
                    teamRepository.save(team);
                    return team;
                })
                .map(teamMapper::teamToTeamDTO)
                .map(teamDTO -> new ResponseEntity<>(
                        teamDTO,
                        HttpStatus.OK
                ))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST    /teams/:id/activate -> activate the "id" team.
     */
    @RequestMapping(value = "/teams/{id}/activate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Timed
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    team.setActivated(true);
                    teamRepository.save(team);
                    return team;
                })
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /teams/:id -> delete the "id" team.
     */
    @RequestMapping(value = "/teams/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       HttpServletRequest request) {
        User user = userService.getUserWithAuthorities();

        if (!request.isUserInRole("ROLE_ADMIN") && !user.getCaptain().getId().equals(id)) {
            throw new AccessDeniedException("You are not allowed to delete this team");
        }

        log.debug("REST request to delete Team : {}", id);
        teamRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("team", id.toString())).build();
    }

}
