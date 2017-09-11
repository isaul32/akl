package fi.tite.akl.service;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.playersummaries.GetPlayerSummaries;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;
import fi.tite.akl.domain.Authority;
import fi.tite.akl.domain.Team;
import fi.tite.akl.domain.User;
import fi.tite.akl.domain.enumeration.Rank;
import fi.tite.akl.dto.UserListDto;
import fi.tite.akl.repository.*;
import fi.tite.akl.security.AuthoritiesConstants;
import fi.tite.akl.security.SecurityUtils;
import fi.tite.akl.service.util.RandomUtil;
import fi.tite.akl.web.rest.errors.CustomParameterizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static fi.tite.akl.security.SteamUserService.convertCommunityIdToSteamId;

/**
 * Service class for managing users.
 */
@Slf4j
@Service
@Transactional
public class UserService {

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private SteamCommunityRepository steamCommunityRepository;

    @Inject
    private TeamRepository teamRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);

        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);

                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(user -> {
               LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
               return user.getResetDate().isAfter(oneDayAgo);
           })
           .map(user -> {
               user.setPassword(passwordEncoder.encode(newPassword));
               user.setResetKey(null);
               user.setResetDate(null);
               userRepository.save(user);
               return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
       return userRepository.findOneByEmail(mail)
           .filter(User::isActivated)
           .map(user -> {
               user.setResetKey(RandomUtil.generateResetKey());
               user.setResetDate(LocalDateTime.now());
               userRepository.save(user);
               return user;
           });
    }

    public User createUser(User newUser) {
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createSteamLoginUser(String communityId, String steamId, String nickname) {
        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        newUser.setLogin(communityId);
        // Steam users won't login by password
        newUser.setPassword(passwordEncoder.encode(RandomUtil.generatePassword()));
        newUser.setDescription("");
        newUser.setCommunityId(communityId);
        newUser.setSteamId(steamId);

        if (nickname != null) {
            newUser.setNickname(nickname.substring(0, Math.min(nickname.length(), 50)));
        }

        // Don't know email yet so can't do the activation routine
        newUser.setActivated(false);
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);

        userRepository.save(newUser);
        log.debug("Created Information for Steam User: {}", newUser);
        return newUser;
    }

    public User createUserInformation(Long id, String login, String password, String firstName, String lastName, String email,
                                      String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setId(id);
        newUser.setLogin(login);
        newUser.setNickname(login);
        // New user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // New user is not active
        newUser.setActivated(false);
        // New user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        newUser.setDescription("");
        newUser.setCreatedBy("");
        authorities.add(authority);
        newUser.setAuthorities(authorities);

        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);

        return newUser;
    }

    public boolean isBirthdateOkay(Date birthdate) {
        if (birthdate == null) {
            return false;
        }

        LocalDate localDate = birthdate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear() > 1970;
    }

    public void updateUserInformation(String nickname, String firstName, String lastName, String email,
                                      Date birthdate, String guild, String description, Rank rank, String langKey,
                                      String activationKey) {

        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            u.setNickname(nickname);
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setBirthdate(isBirthdateOkay(birthdate) ? birthdate : null);
            u.setGuild(guild);
            u.setDescription(description);
            u.setRank(rank);
            u.setLangKey(langKey);
            u.setActivationKey(activationKey);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u-> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    public void changeLogin(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            throw new CustomParameterizedException("Login name is already used");
        });

        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u-> {
            u.setLogin(login);
            userRepository.save(u);
            log.debug("Changed login for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        Optional<User> user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin());

        if (user.isPresent()) {
            User currentUser = user.get();
            currentUser.getAuthorities().size(); // eagerly load the association
            return currentUser;
        }

        return null;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        Optional<User> user = userRepository.findOneById(id);

        if (user.isPresent()) {
            User currentUser = user.get();
            currentUser.getAuthorities().size(); // eagerly load the association
            return currentUser;
        }

        return null;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(String communityId) {
        Optional<User> user = userRepository.findOneByCommunityId(communityId);

        if (user.isPresent()) {
            User currentUser = user.get();
            currentUser.getAuthorities().size(); // eagerly load the association
            return currentUser;
        }

        return null;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    /*@Scheduled(cron = "0 0 1 * * ?")*/
    public void removeNotActivatedUsers() {
        LocalDateTime now = LocalDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }

    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public void deleteUser(Long id) {
        User user = userRepository.getOne(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @Transactional(readOnly = true)
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    public void updateUserAuthorities(Long id, Set<Authority> authorities) {
        User user = userRepository.getOne(id);
        if (user != null) {
            user.setAuthorities(authorities);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Transactional(readOnly = true)
    public Set<Authority> getUserAuthorityBySteamId(String steamId) {
        return userRepository.findOneBySteamId(steamId)
                .map(this::getUseAuthority)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Set<Authority> getUserAuthorityByCommunityId(String communityId) {
        return userRepository.findOneByCommunityId(communityId)
                .map(this::getUseAuthority)
                .orElse(null);
    }

    public void addMultipleUsers(UserListDto users) {
        users.getUsers().forEach(dto -> {
            log.info(dto.getCommunityId());
            
            String communityId = dto.getCommunityId();
            User user = getUserWithAuthorities(communityId);

            // Add user if not exist
            if (user == null) {
                try {
                    String steamId = convertCommunityIdToSteamId(Long.parseUnsignedLong(communityId));

                    GetPlayerSummaries summaries = steamCommunityRepository.findSteamUser(communityId);
                    Optional<Player> player = summaries.getResponse().getPlayers().stream().findFirst();

                    if (player.isPresent()) {
                        user = createSteamLoginUser(communityId, steamId,
                                player.get().getPersonaname());
                    } else {
                        user = createSteamLoginUser(communityId, steamId, null);
                    }

                    String nickname = dto.getNickname();
                    if (!StringUtils.isEmpty(nickname)) {
                        user.setNickname(nickname);
                    }

                    String firstName = dto.getFirstName();
                    if (!StringUtils.isEmpty(firstName)) {
                        user.setFirstName(firstName);
                    }

                    String lastName = dto.getLastName();
                    if (!StringUtils.isEmpty(lastName)) {
                        user.setLastName(lastName);
                    }

                    String email = dto.getEmail();
                    if (!StringUtils.isEmpty(email)) {
                        user.setEmail(email);
                    }

                    String guild = dto.getGuild();
                    if (!StringUtils.isEmpty(guild)) {
                        user.setGuild(guild);
                    }
                } catch (NumberFormatException | SteamApiException e) {
                    log.error(e.getLocalizedMessage());
                }
            }

            // Add the user to correct team
            Team team = null;
            Long captain = dto.getCaptainId();
            if (captain != null) {
                team = teamRepository.findOne(captain);
                if (team != null && team.getCaptain() == null) {
                    team.setCaptain(user);
                    team.getMembers().add(user);
                }
            }

            Long member = dto.getMemberId();
            if (member != null) {
                team = teamRepository.findOne(member);

                if (team != null && !team.getMembers().contains(user)) {
                    team.getMembers().add(user);
                }
            }

            Long standin = dto.getStandinId();
            if (standin != null) {
                team = teamRepository.findOne(standin);

                if (team != null && !team.getMembers().contains(user)) {
                    team.getMembers().add(user);
                }
            }
        });
    }

    private Set<Authority> getUseAuthority(User user) {
        Set<Authority> authorities = user.getAuthorities();

        if (authorities.size() > 0) {
            return authorities;
        }

        return null;
    }
}
