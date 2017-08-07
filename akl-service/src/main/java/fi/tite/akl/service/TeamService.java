package fi.tite.akl.service;

import fi.tite.akl.domain.Season;
import fi.tite.akl.domain.Team;
import fi.tite.akl.domain.User;
import fi.tite.akl.repository.SeasonRepository;
import fi.tite.akl.repository.TeamRepository;
import fi.tite.akl.repository.UserRepository;
import fi.tite.akl.security.AuthoritiesConstants;
import fi.tite.akl.web.rest.errors.CustomParameterizedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TeamService {

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private SeasonRepository seasonRepository;

    @PreAuthorize("isAuthenticated()")
    public Team create(Team team) {
        User currentUser = userService.getUserWithAuthorities();

        team.setActivated(false);
        team.setSeason(seasonRepository.findByArchived(false));
        team.setCaptain(currentUser);
        team.getMembers().add(currentUser);

        return teamRepository.save(team);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Team> activate(Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    if (team.getMembers().size() < 5) {
                        throw new CustomParameterizedException("Team doesn't have enough member");
                    }

                    team.setActivated(true);

                    // Todo: Send team activated mail to members or captain

                    return teamRepository.save(team);
                });
    }

    @PreAuthorize("isAuthenticated()")
    public void delete(Long id) {
        Team team = teamRepository.findOne(id);
        if (team != null) {
            team.setCaptain(null);
            team.setMembers(null);
            team.setRequests(null);
            teamRepository.delete(team);
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void requestInvite(Long id) {
        User currentUser = userService.getUserWithAuthorities();

        if (!currentUser.isActivated()) {
            throw new CustomParameterizedException("Can't join a team without an email set and activated");
        }

        Season activeSeason = seasonRepository.findByArchived(false);
        if (activeSeason == null) {
            throw new CustomParameterizedException("Team doesn't belong in active season");
        }

        Optional<Team> currentTeam = Optional.ofNullable(teamRepository
                .findOneByMembersIdAndSeasonId(currentUser.getId(), activeSeason.getId()));
        if (currentTeam.isPresent()) {
            throw new CustomParameterizedException("You can't join multiple teams");
        }

        Team team = teamRepository.findOne(id);
        if (team == null) {
            throw new CustomParameterizedException("Team does not exist");
        }

        if (team.getCaptain().equals(currentUser) || team.getMembers().stream()
                .anyMatch(user -> user.equals(currentUser))) {
            throw new CustomParameterizedException("You are already member of the team");
        }

        List<User> requests = team.getRequests();
        requests.add(currentUser);
        team.setRequests(requests);
        teamRepository.save(team);
    }

    @PreAuthorize("isAuthenticated()")
    public List<User> getRequests(Long id) {
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
                .orElse(new ArrayList<>());
    }

    @PreAuthorize("isAuthenticated()")
    public void declineRequest(Long id, long userId) {
        Team team = teamRepository.findOne(id);
        if (team == null) {
            throw new AccessDeniedException("Team does not exist");
        }

        User user = userService.getUserWithAuthorities();
        if (user == null) {
            throw new AccessDeniedException("You are not allowed to decline request");
        }

        if (user.getAuthorities().stream()
                .noneMatch(authority -> authority.getName().equals(AuthoritiesConstants.ADMIN))
                && !team.getCaptain().equals(user)) {
            throw new AccessDeniedException("You are not allowed to decline membership requests");
        }

        User declinedMember = userRepository.findOne(userId);
        if (declinedMember == null) {
            throw new AccessDeniedException("User does not exist");
        }

        List<User> requests = team.getRequests();
        requests.remove(declinedMember);
        teamRepository.save(team);
    }
}
