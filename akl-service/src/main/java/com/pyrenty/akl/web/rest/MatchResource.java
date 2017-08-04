package com.pyrenty.akl.web.rest;

import com.pyrenty.akl.domain.Group;
import com.pyrenty.akl.domain.MatchRequest;
import com.pyrenty.akl.domain.Team;
import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.repository.GroupRepository;
import com.pyrenty.akl.repository.MatchRequestRepository;
import com.pyrenty.akl.repository.TeamRepository;
import com.pyrenty.akl.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET)
    public List<MatchRequest> getAll() {
        User user = userService.getUserWithAuthorities();

        return Optional.ofNullable(teamRepository.findOneForUser(user.getId()))
                .map(team -> matchRequestRepository.findByTeam1OrTeam2(team, team))
                .orElse(new ArrayList<>());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<MatchRequest> create(@RequestBody MatchRequest matchRequest) {

        User user = userService.getUserWithAuthorities();
        if (!user.getCaptain().getId().equals(matchRequest.getTeam1().getId())) {
            throw new AccessDeniedException("You are not allowed to make match request");
        }

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
