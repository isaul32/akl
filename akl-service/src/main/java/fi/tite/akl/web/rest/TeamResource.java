package fi.tite.akl.web.rest;

import fi.tite.akl.domain.CalendarEvent;
import fi.tite.akl.domain.Team;
import fi.tite.akl.domain.User;
import fi.tite.akl.domain.enumeration.MembershipRole;
import fi.tite.akl.dto.TeamDto;
import fi.tite.akl.dto.TeamRequestDto;
import fi.tite.akl.dto.UserPublicDto;
import fi.tite.akl.repository.CalendarEventRepository;
import fi.tite.akl.repository.SeasonRepository;
import fi.tite.akl.repository.TeamRepository;
import fi.tite.akl.repository.UserRepository;
import fi.tite.akl.security.AuthoritiesConstants;
import fi.tite.akl.security.InvalidRoleException;
import fi.tite.akl.security.SecurityUtils;
import fi.tite.akl.service.TeamService;
import fi.tite.akl.service.UserService;
import fi.tite.akl.web.rest.errors.CustomParameterizedException;
import fi.tite.akl.web.rest.mapper.TeamMapper;
import fi.tite.akl.web.rest.mapper.UserMapper;
import fi.tite.akl.web.rest.util.HeaderUtil;
import fi.tite.akl.web.rest.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping("/api/teams")
public class TeamResource {

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
    private SeasonRepository seasonRepository;

    @Inject
    private CalendarEventRepository eventRepository;

    /*
    @PreAuthorize("isAuthenticated()")
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

        Team team = teamService.create(teamMapper.teamDtoToTeam(teamDto));

        return ResponseEntity.created(new URI("/api/teams/" + team.getId()))
                .headers(HeaderUtil.createAlert("Team created", team.getId().toString()))
                .body(teamMapper.teamToTeamDto(team));
    }
    */

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TeamDto>> getAll(
            @RequestParam(value = "page", required = false) Integer offset,
            @RequestParam(value = "per_page", required = false) Integer limit,
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            @RequestParam(value = "season", required = false) Long season,
            @RequestParam(value = "sort_propery", required = false, defaultValue = "name") String sortPropery,
            @RequestParam(value = "sort_asc", required = false, defaultValue = "true") boolean sortAsc
    ) throws URISyntaxException {
        log.debug("REST request to get all Teams");

        Pageable paging = PaginationUtil.generatePageRequest(offset, limit, new Sort(
                new Sort.Order(sortAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortPropery)
        ));

        if (season == null) {
            season = seasonRepository.findByArchived(false).getId();
        }

        Page<Team> page = teamRepository.findBySeasonIdAndNameContainingIgnoreCase(season, filter, paging);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);

        return new ResponseEntity<>(page.getContent().stream()
                .map(teamMapper::teamToTeamDtoWithoutMembers)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<TeamDto> get(@PathVariable Long id) {
        log.debug("REST request to get Team : {}", id);

        return teamService.get(id)
                .map(teamMapper::teamToTeamDto)
                .map(teamDto -> {
                    User user = userService.getUserWithAuthorities();
                    // If user is not admin or team captain, hide application.
                    if (user == null
                            || user.getAuthorities().stream().noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN))
                            || teamDto.getCaptain() != null
                            || teamDto.getCaptain().getId().equals(user.getId())) {
                        teamDto.setApplication(null);
                    }

                    return teamDto;
                })
                .map(teamDTO -> new ResponseEntity<>(teamDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TeamDto> update(@PathVariable Long id, @Valid @RequestBody TeamDto teamDto) {
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
                        team.setApplication(teamDto.getApplication());
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
    public ResponseEntity<Void> activate(@PathVariable Long id) {
        return Optional.ofNullable(teamService.activate(id))
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

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

                        // If captain, delete whole team
                        if (team.getCaptain().getId().equals(user.getId())) {
                            teamService.delete(id);
                            return new ResponseEntity<Team>(HttpStatus.OK);
                        } else if (team.getMembers().stream().anyMatch(member -> member.equals(user))) {
                            //user.getTeams().remove(team);
                            team.getMembers().remove(user);
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
    public ResponseEntity<Void> requestInvite(@PathVariable Long id) {
        User currentUser = userService.getUserWithAuthorities();
        if (!currentUser.isActivated()) {
            HttpHeaders headers = HeaderUtil.createAlert("request", "", "Can't join a team without an email set and activated");
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }

        Optional<Team> currentTeam = Optional.ofNullable(teamRepository.findOneByMembersId(currentUser.getId()));
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
    public ResponseEntity<List<UserPublicDto>> getRequests(@PathVariable Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    User user = userService.getUserWithAuthorities();

                    // User must be captain of the team
                    if (user == null || team.getCaptain() == null || !team.getCaptain().equals(user)) {
                        throw new AccessDeniedException("You are not allowed to see membership requests");
                    }

                    return team;
                })
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
    public ResponseEntity<Void> approveRequest(@PathVariable Long id,
                                               @PathVariable Long userId,
                                               @Valid @RequestBody TeamRequestDto teamRequest,
                                               HttpServletRequest request) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    User user = userService.getUserWithAuthorities();

                    if (!request.isUserInRole(AuthoritiesConstants.ADMIN) && !team.getCaptain().equals(user)) {
                        throw new AccessDeniedException("You are not allowed to accept membership requests");
                    }

                    if (!teamRequest.getRole().equals(MembershipRole.ROLE_MEMBER)) {
                        throw new InvalidRoleException();
                    }

                    User newMember = userRepository.findOne(userId);
                    if (newMember == null) {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }

                    // Check maximum members
                    if (team.getMembers().size() >= 7) {
                        throw new CustomParameterizedException("Team have maximum amount of members");
                    }

                    if (team.isActivated()) {
                        throw new CustomParameterizedException("Cannot add member to activated team");
                    }

                    team.getMembers().add(newMember);
                    Set<User> requests = team.getRequests();
                    requests.remove(newMember);
                    teamRepository.save(team);

                    return team;
                })
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}/requests/{userId}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> declineRequest(@PathVariable Long id,
                                               @PathVariable long userId,
                                               HttpServletRequest request) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    User user = userService.getUserWithAuthorities();

                    if (!request.isUserInRole(AuthoritiesConstants.ADMIN) && !team.getCaptain().equals(user)) {
                        throw new AccessDeniedException("You are not allowed to decline membership requests");
                    }

                    User declinedMember = userRepository.findOne(userId);
                    if (declinedMember == null) {
                        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                    }

                    Set<User> requests = team.getRequests();
                    requests.remove(declinedMember);
                    teamRepository.save(team);

                    return team;
                })
                .map(team -> new ResponseEntity<Void>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Team : {}", id);

        teamService.delete(id);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Team deleted", id.toString())).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}/schedule", method = RequestMethod.GET)
    public ResponseEntity<List<CalendarEvent>> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(eventRepository.findAll());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/{id}/schedule", method = RequestMethod.POST)
    public ResponseEntity<List<CalendarEvent>> updateSchedule(@PathVariable Long id,
                                                              @RequestBody Set<CalendarEvent> events) {

        return ResponseEntity.ok(eventRepository.save(events));
    }
}
