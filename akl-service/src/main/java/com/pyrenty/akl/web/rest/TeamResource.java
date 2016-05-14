package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Team;
import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.service.TeamService;
import com.pyrenty.akl.domain.enumeration.MembershipRoles;
import com.pyrenty.akl.domain.user.User;
import com.pyrenty.akl.repository.TeamRepository;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.security.AuthoritiesConstants;
import com.pyrenty.akl.security.InvalidRoleException;
import com.pyrenty.akl.service.UserService;
import com.pyrenty.akl.web.rest.dto.TeamRequestDTO;
import com.pyrenty.akl.web.rest.dto.UserBaseDTO;
import com.pyrenty.akl.web.rest.dto.UserDTO;
import com.pyrenty.akl.web.rest.errors.CustomParameterizedException;
import com.pyrenty.akl.web.rest.mapper.UserMapper;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import com.pyrenty.akl.web.rest.dto.TeamDTO;
import com.pyrenty.akl.web.rest.mapper.TeamMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister;
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
import java.util.Set;
import java.util.stream.Collector;
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

    @Inject
    private UserMapper userMapper;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/teams", method = RequestMethod.POST)
    @Transactional
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
    @Transactional
    @Timed
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        return Optional.ofNullable(teamService.activate(id))
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/teams/{id}/requests",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<Void> requestInvite(@PathVariable Long id) {
        User currentUser = userService.getUserWithAuthorities();

        if (currentUser.getEmail() == null) {
            throw new CustomParameterizedException("Can't join a team without an email set");
        }

        Optional<Team> currentTeam = Optional.ofNullable(teamRepository.findOneForUser(currentUser.getId()));

        if (currentTeam.isPresent()) {
            return ResponseEntity.badRequest().header("Failure", "You can't join multiple teams").body(null);
        }

        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    Set<User> requests = team.getRequests();
                    requests.add(currentUser);
                    team.setRequests(requests);
                    teamRepository.save(team);
                    return team;
                })
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/teams/{id}/requests",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<List<UserBaseDTO>> getRequests(@PathVariable Long id,
                                                         HttpServletRequest request) {
        User user = userService.getUserWithAuthorities();

        if (!request.isUserInRole("ROLE_ADMIN") && !user.getCaptain().getId().equals(id)) {
            throw new AccessDeniedException("You are not allowed to see membership requests");
        }

        Optional<Team> team = Optional.ofNullable(teamRepository.findOne(id));

        if (!team.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(team.get().getRequests().stream()
                .map(userMapper::userToUserBaseDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @RequestMapping(value = "/teams/{id}/requests/{userId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<Void> approveRequest(@PathVariable Long id,
                                               @PathVariable Long userId,
                                               @Valid @RequestBody TeamRequestDTO teamRequest,
                                               HttpServletRequest request) {
        User user = userService.getUserWithAuthorities();

        if (!request.isUserInRole("ROLE_ADMIN") && !user.getCaptain().getId().equals(id)) {
            throw new AccessDeniedException("You are not allowed to accept membership requests");
        }

        if (!teamRequest.getRole().equals(MembershipRoles.ROLE_MEMBER.toString()) &&
                !teamRequest.getRole().equals(MembershipRoles.ROLE_STANDIN.toString())) {
            throw new InvalidRoleException();
        }

        User newMember = userRepository.findOne(userId);

        if (newMember == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    Set<User> requests = team.getRequests();
                    requests.remove(newMember);
                    teamRepository.save(team);

                    if (teamRequest.getRole().equals(MembershipRoles.ROLE_MEMBER.toString())) {
                        newMember.setMember(team);
                    } else if (teamRequest.getRole().equals(MembershipRoles.ROLE_STANDIN.toString())) {
                        newMember.setStandin(team);
                    }

                    userRepository.save(newMember);
                    return team;
                })
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/teams/{id}/requests/{userId}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<Void> declineRequest(@PathVariable Long id,
                                               @PathVariable long userId,
                                               HttpServletRequest request) {
        User user = userService.getUserWithAuthorities();

        if (!request.isUserInRole("ROLE_ADMIN") && !user.getCaptain().getId().equals(id)) {
            throw new AccessDeniedException("You are not allowed to decline membership requests");
        }

        User declinedMember = userRepository.findOne(userId);

        if (declinedMember == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    Set<User> requests = team.getRequests();
                    requests.remove(declinedMember);

                    teamRepository.save(team);
                    return team;
                })
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/teams/{id}", method = RequestMethod.DELETE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);

        teamService.delete(id);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("team", id.toString())).build();
    }

}
