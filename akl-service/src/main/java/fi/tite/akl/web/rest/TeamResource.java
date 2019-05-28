package fi.tite.akl.web.rest;

import fi.tite.akl.domain.CalendarEvent;
import fi.tite.akl.domain.Team;
import fi.tite.akl.domain.User;
import fi.tite.akl.dto.TeamDto;
import fi.tite.akl.dto.TeamRequestDto;
import fi.tite.akl.dto.UserPublicDto;
import fi.tite.akl.mapper.TeamMapper;
import fi.tite.akl.mapper.UserMapper;
import fi.tite.akl.repository.CalendarEventRepository;
import fi.tite.akl.repository.SeasonRepository;
import fi.tite.akl.repository.TeamRepository;
import fi.tite.akl.repository.UserRepository;
import fi.tite.akl.security.AuthoritiesConstants;
import fi.tite.akl.service.TeamService;
import fi.tite.akl.service.UserService;
import fi.tite.akl.web.rest.errors.CustomParameterizedException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
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

    
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TeamDto> create(@Valid @RequestBody TeamDto teamDto) throws URISyntaxException {
        log.debug("REST request to save Team : {}", teamDto);

        User user = userService.getUserWithAuthorities();

        if (!user.isActivated()) {
            throw new CustomParameterizedException("Can't create a team without an email set and activated");
        }

        // Check if player is in team at current season
        user.getTeams().forEach(team -> {
                    if (!team.getSeason().isArchived()) {
                        throw new CustomParameterizedException("Player is already in " + team.getName() + " team");
                    }
                });

        Team team = teamService.create(teamMapper.teamDtoToTeam(teamDto));

        return ResponseEntity.created(new URI("/#/teams/" + team.getId()))
                .headers(HeaderUtil.createAlert("Team created", team.getId().toString()))
                .body(teamMapper.teamToTeamDto(team));
    }
    

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<TeamDto>> getAll(
            @RequestParam(value = "page", required = false) Integer offset,
            @RequestParam(value = "per_page", required = false) Integer limit,
            @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            @RequestParam(value = "season", required = false) Long season,
            @RequestParam(value = "sort_property", required = false, defaultValue = "name") String sortProperty,
            @RequestParam(value = "sort_asc", required = false, defaultValue = "true") boolean sortAsc
    ) throws URISyntaxException {
        log.debug("REST request to get all Teams");

        Pageable paging = PaginationUtil.generatePageRequest(offset, limit, new Sort(
                new Sort.Order(sortAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortProperty)
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

        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    User user = userService.getUserWithAuthorities();

                    // Check if user has permission to see team's application
                    boolean allowed = false;
                    if (user != null && user.getAuthorities().stream()
                            .anyMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN))) {
                        allowed = true;
                    }
                    if (team.getCaptain() != null && team.getCaptain().equals(user)) {
                        allowed = true;
                    }

                    if (!allowed) {
                        team.setApplication(null);
                    }

                    return team;
                })
                .map(teamMapper::teamToTeamDto)
                .map(ResponseEntity::ok)
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

                    // Check if team is already activated
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
    public ResponseEntity<TeamDto> leaveTeam(@PathVariable Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    if (team.isActivated()) {
                        throw new CustomParameterizedException("Leaving from activated team is not allowed");
                    }

                    User currentUser = userService.getUserWithAuthorities();

                    // If captain, delete whole team
                    if (team.getCaptain().equals(currentUser)) {
                        teamService.delete(id);
                    } else if (team.getMembers().stream().anyMatch(member -> member.equals(currentUser))) {
                        team.getMembers().remove(currentUser);
                        teamRepository.save(team);
                    }

                    return new ResponseEntity<>(teamMapper.teamToTeamDto(team), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}/requests", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity requestInvite(@PathVariable Long id) {
        teamService.requestInvite(id);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Request sent", id.toString())).build();
    }

    @RequestMapping(value = "/{id}/requests", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UserPublicDto>> getRequests(@PathVariable Long id) {
        return Optional.ofNullable(teamService.getRequests(id))
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
                                               @Valid @RequestBody TeamRequestDto teamRequest) {
        teamService.approveRequest(id, userId, teamRequest);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Request approved", id.toString())).build();
    }

    @RequestMapping(value = "/{id}/requests/{userId}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity declineRequest(@PathVariable Long id,
                                               @PathVariable long userId) {

        teamService.declineRequest(id, userId);

        return new ResponseEntity<Void>(HttpStatus.OK);
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
