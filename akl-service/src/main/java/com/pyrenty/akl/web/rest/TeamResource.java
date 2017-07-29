package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.CalendarEvent;
import com.pyrenty.akl.domain.Team;
import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.domain.enumeration.MembershipRole;
import com.pyrenty.akl.dto.TeamDto;
import com.pyrenty.akl.dto.TeamRequestDto;
import com.pyrenty.akl.dto.UserPublicDto;
import com.pyrenty.akl.repository.CalendarEventRepository;
import com.pyrenty.akl.repository.TeamRepository;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.security.InvalidRoleException;
import com.pyrenty.akl.security.SecurityUtils;
import com.pyrenty.akl.service.TeamService;
import com.pyrenty.akl.service.UserService;
import com.pyrenty.akl.web.rest.errors.CustomParameterizedException;
import com.pyrenty.akl.web.rest.mapper.TeamMapper;
import com.pyrenty.akl.web.rest.mapper.UserMapper;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * REST controller for managing Team.
 */
@RestController
@RequestMapping("/api/teams")
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

    @Inject
    private TeamService teamService;

    @Inject
    private CalendarEventRepository eventRepository;

    /*@PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST)
    @Timed
    public ResponseEntity<TeamDto> create(@Valid @RequestBody TeamDto teamDto) throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDto);

        User user = userService.getUserWithAuthorities();

        if (!user.isActivated()) {
            throw new CustomParameterizedException("Can't create a team without an email set and activated");
        } else if (teamDto.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new team cannot already have an ID").body(null);
        }

        Team team = teamService.create(teamMapper.teamDTOToTeam(teamDto));

        return ResponseEntity.created(new URI("/api/teams/" + team.getId()))
                .headers(HeaderUtil.createAlert("Team created", team.getId().toString()))
                .body(teamMapper.teamToTeamDTO(team));
    }*/

    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<TeamDto>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                                @RequestParam(value = "per_page", required = false) Integer limit,
                                                @RequestParam(value = "season", required = false) Long season,
                                                HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to get all Teams");

        Pageable paging = PaginationUtil.generatePageRequest(offset, limit, new Sort(
                new Sort.Order(Sort.Direction.ASC, "tag")
        ));
        Page<Team> page = teamRepository.findBySeasonId(season, paging);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(page.getContent().stream()
                .map(teamMapper::teamToTeamDtoWithoutMembers)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<TeamDto> get(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);

        return teamService.get(id)
                .map(teamMapper::teamToTeamDto)
                .map(teamDTO -> new ResponseEntity<>(teamDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<TeamDto> update(@PathVariable Long id,
                                          @Valid @RequestBody TeamDto teamDto) {
        log.debug("REST request to put Team : {}", id);

        return Optional.ofNullable(teamRepository.findOne(teamDto.getId()))
                .map(team -> {
                    User user = userService.getUserWithAuthorities();
                    // Check if user is captain
                    if (!team.getCaptain().getId().equals(user.getId())) {
                        throw new CustomParameterizedException("You must be captain of the team.");
                    }

                    return team;
                })
                .map(team -> {
                    if (!team.isActivated()) {
                        team.setName(teamDto.getName());
                        team.setTag(teamDto.getTag());
                        team.setRepresenting(teamDto.getRepresenting());
                    }
                    team.setRank(teamDto.getRank());
                    team.setDescription(teamDto.getDescription());
                    teamRepository.save(team);

                    return team;
                })
                .map(teamMapper::teamToTeamDto)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}/activate", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        return Optional.ofNullable(teamService.activate(id))
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Timed
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/{id}/leave", method = RequestMethod.POST)
    public ResponseEntity<Team> leaveTeam(@PathVariable Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {

                    if (team.isActivated()) {
                        throw new CustomParameterizedException("Leaving from activated team is not allowed");
                    }

                    Optional<User> userOptional = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());
                    if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        if (team.getCaptain().getId().equals(user.getId())) {
                            teamService.delete(id);
                            return new ResponseEntity<Team>(HttpStatus.OK);
                        }
                        if (user.getMember() != null && user.getMember().getId().equals(team.getId())) {
                            user.setMember(null);
                            team.getMembers().remove(user);
                            userRepository.save(user);
                            teamRepository.save(team);
                        }
                        if (user.getStandin() != null && user.getStandin().getId().equals(team.getId())) {
                            user.setStandin(null);
                            team.getStandins().remove(user);
                            userRepository.save(user);
                            teamRepository.save(team);
                        }
                    }
                    return new ResponseEntity<>(team, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}/requests", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<Void> requestInvite(@PathVariable Long id) {
        User currentUser = userService.getUserWithAuthorities();
        if (!currentUser.isActivated()) {
            HttpHeaders headers = HeaderUtil.createAlert("request", "", "Can't join a team without an email set and activated");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        Optional<Team> currentTeam = Optional.ofNullable(teamRepository.findOneForUser(currentUser.getId()));
        if (currentTeam.isPresent()) {
            HttpHeaders headers = HeaderUtil.createAlert("request", "", "You can't join multiple teams");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
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

    @RequestMapping(value = "/{id}/requests", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<List<UserPublicDto>> getRequests(@PathVariable Long id) {
        User user = userService.getUserWithAuthorities();

        // Must be team captain
        if (user == null || user.getCaptain() == null || !user.getCaptain().getId().equals(id)) {
            throw new AccessDeniedException("You are not allowed to see membership requests");
        }

        return Optional.ofNullable(teamRepository.findOne(id))
                .map(Team::getRequests)
                .map(requests -> requests.stream()
                        .map(userMapper::userToUserPublicDto)
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}/requests/{userId}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("isAuthenticated()")
    @Timed
    public ResponseEntity<Void> approveRequest(@PathVariable Long id,
                                               @PathVariable Long userId,
                                               @Valid @RequestBody TeamRequestDto teamRequest,
                                               HttpServletRequest request) {
        User user = userService.getUserWithAuthorities();

        if (!request.isUserInRole("ROLE_ADMIN") && !user.getCaptain().getId().equals(id)) {
            throw new AccessDeniedException("You are not allowed to accept membership requests");
        }

        if (!teamRequest.getRole().equals(MembershipRole.ROLE_MEMBER.toString()) &&
                !teamRequest.getRole().equals(MembershipRole.ROLE_STANDIN.toString())) {
            throw new InvalidRoleException();
        }

        User newMember = userRepository.findOne(userId);
        if (newMember == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    // Check maximum members and standins count
                    if (teamRequest.getRole().equals(MembershipRole.ROLE_MEMBER.toString()) && team.getMembers().size() >= 4) {
                        throw new CustomParameterizedException("Team have maximum amount of members");
                    }

                    if (teamRequest.getRole().equals(MembershipRole.ROLE_STANDIN.toString()) && team.getStandins().size() >= 2) {
                        throw new CustomParameterizedException("Team have maximum amount of standins");
                    }

                    if (teamRequest.getRole().equals(MembershipRole.ROLE_MEMBER.toString()) && team.isActivated()) {
                        throw new CustomParameterizedException("Cannot add member to activated team");
                    }

                    return team;
                })
                .map(team -> {
                    Set<User> requests = team.getRequests();
                    requests.remove(newMember);
                    teamRepository.save(team);

                    if (teamRequest.getRole().equals(MembershipRole.ROLE_MEMBER.toString())) {
                        newMember.setMember(team);
                    } else if (teamRequest.getRole().equals(MembershipRole.ROLE_STANDIN.toString())) {
                        newMember.setStandin(team);
                    }

                    userRepository.save(newMember);
                    return team;
                })
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}/requests/{userId}", method = RequestMethod.DELETE)
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

    @Timed
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);

        teamService.delete(id);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Team deleted", id.toString())).build();
    }

    @Timed
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}/schedule", method = RequestMethod.GET)
    public ResponseEntity<List<CalendarEvent>> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @Timed
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}/schedule", method = RequestMethod.POST)
    public ResponseEntity<List<CalendarEvent>> updateSchedule(@PathVariable Long id, @RequestBody Set<CalendarEvent> events) {

        return ResponseEntity.ok(eventRepository.save(events));
    }
}
