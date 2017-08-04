package fi.tite.akl.service;

import fi.tite.akl.domain.Team;
import fi.tite.akl.domain.User;
import fi.tite.akl.repository.SeasonRepository;
import fi.tite.akl.repository.TeamRepository;
import fi.tite.akl.repository.UserRepository;
import fi.tite.akl.web.rest.errors.CustomParameterizedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        team.setActivated(false);
        team.setSeason(seasonRepository.findByArchived(false));
        Team result = teamRepository.save(team);
        User currentUser = userService.getUserWithAuthorities();
        currentUser.setCaptain(result);
        userRepository.save(currentUser);

        return result;
    }

    @Transactional(readOnly = true)
    public Optional<Team> get(Long id) {
        return Optional.ofNullable(teamRepository.findOne(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Team> activate(Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    if (team.getMembers().size() < 4) {
                        throw new CustomParameterizedException("Team doesn't have enough member");
                    }

                    team.setActivated(true);

                    // todo: Send team activated mail to members or captain

                    return teamRepository.save(team);
                });
    }

    public void delete(Long id) {
        Team team = teamRepository.findOne(id);
        if (team != null) {
            // Captain
            User captain = team.getCaptain();
            captain.setCaptain(null);
            userRepository.save(captain);

            // Members
            Set<User> members = team.getMembers().stream()
                    .peek(m -> m.setMember(null))
                    .collect(Collectors.toSet());
            userRepository.save(members);

            // Standins
            Set<User> standins = team.getStandins().stream()
                    .peek(s -> s.setStandin(null))
                    .collect(Collectors.toSet());
            userRepository.save(standins);

            // Team
            team.setCaptain(null);
            team.setMembers(null);
            team.setStandins(null);

            team.setRequests(null);
            teamRepository.delete(team);
        }
    }
}
