package com.pyrenty.akl.service;

import com.pyrenty.akl.domain.Team;
import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.repository.TeamRepository;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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

    @PreAuthorize("isAuthenticated()")
    public Team create(Team team) {
        team.setActivated(false);
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

    @Transactional(readOnly = true)
    public Page<Team> getAll(Pageable pageable) {
        Page<Team> page = teamRepository.findAll(pageable);
        page.getContent().stream().forEach(team -> {
            team.getMembers().size();
            team.getStandins().size();
        });
        return page;
    }

    @PreAuthorize("isAuthenticated()")
    public Team update(Team team) {

        String login = SecurityUtils.getCurrentLogin();
        if (!SecurityUtils.isUserInRole("ADMIN") && !team.getCaptain().getLogin().equals(login)) {
            throw new AccessDeniedException("You are not allowed to edit this team");
        }

        return teamRepository.save(team);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Team> activate(Long id) {
        return Optional.ofNullable(teamRepository.findOne(id))
                .map(team -> {
                    team.setActivated(true);
                    return teamRepository.save(team);
                });
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        teamRepository.delete(id);
    }
}
