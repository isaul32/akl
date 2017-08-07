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

    public void delete(Long id) {
        Team team = teamRepository.findOne(id);
        if (team != null) {
            team.setCaptain(null);
            team.setMembers(null);
            team.setRequests(null);
            teamRepository.delete(team);
        }
    }
}
