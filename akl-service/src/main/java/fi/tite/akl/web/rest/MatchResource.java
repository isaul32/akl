package fi.tite.akl.web.rest;

import fi.tite.akl.domain.*;
import fi.tite.akl.repository.GroupRepository;
import fi.tite.akl.repository.MatchRequestRepository;
import fi.tite.akl.repository.SeasonRepository;
import fi.tite.akl.repository.TeamRepository;
import fi.tite.akl.service.UserService;
import fi.tite.akl.web.rest.errors.CustomParameterizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/matches")
public class MatchResource {

    @Inject
    private MatchRequestRepository matchRequestRepository;

    @Inject
    private UserService userService;

    @Inject
    private GroupRepository groupRepository;

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private SeasonRepository seasonRepository;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET)
    public List<MatchRequest> getAll() {
        User user = userService.getUserWithAuthorities();

        Season activeSeason = seasonRepository.findByArchived(false);
        if (activeSeason == null) {
            throw new CustomParameterizedException("Team doesn't belong in active season");
        }

        return Optional.ofNullable(teamRepository.findOneByMembersIdAndSeasonId(user.getId(), activeSeason.getId()))
                .map(team -> matchRequestRepository.findByTeam1OrTeam2(team, team))
                .orElse(new ArrayList<>());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MatchRequest> create(@RequestBody MatchRequest matchRequest) {

        User user = userService.getUserWithAuthorities();
        /*if (!user.getCaptain().getId().equals(matchRequest.getTeam1().getId())) {
            throw new AccessDeniedException("You are not allowed to make match request");
        }*/

        Team ownTeam = matchRequest.getTeam1();
        Team otherTeam = matchRequest.getTeam2();

        return groupRepository.findByTeams_id(ownTeam.getId())
                .map(group -> {

                    boolean teamsAreInSameGroup = false;
                    Optional<Group> optGroup = groupRepository.findByTeams_id(ownTeam.getId());
                    if (optGroup.isPresent()) {
                        teamsAreInSameGroup = optGroup.get().getTeams().stream()
                                .anyMatch(team -> team.getId().equals(otherTeam.getId()));
                    }

                    if (teamsAreInSameGroup) {
                        return ResponseEntity.ok(matchRequestRepository.save(matchRequest));
                    } else {
                        return new ResponseEntity<MatchRequest>(HttpStatus.BAD_REQUEST);
                    }
                })
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
