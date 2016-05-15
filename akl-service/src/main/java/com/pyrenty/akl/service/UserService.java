package com.pyrenty.akl.service;

import com.pyrenty.akl.domain.Authority;
import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.domain.enumeration.Rank;
import com.pyrenty.akl.repository.AuthorityRepository;
import com.pyrenty.akl.repository.PersistentTokenRepository;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.security.SecurityUtils;
import com.pyrenty.akl.service.util.RandomUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(user -> {
               DateTime oneDayAgo = DateTime.now().minusHours(24);
               return user.getResetDate().isAfter(oneDayAgo.toInstant().getMillis());
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
               user.setResetDate(DateTime.now());
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
        Authority authority = authorityRepository.findOne("ROLE_USER");
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
        newUser.setActivated(true);
        authorities.add(authority);
        newUser.setAuthorities(authorities);

        userRepository.save(newUser);
        log.debug("Created Information for Steam User: {}", newUser);
        return newUser;
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        newUser.setDescription("");
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUserInformation(String nickname, String firstName, String lastName, String email, DateTime birthdate,
                                      String guild, String description, Rank rank, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            u.setNickname(nickname);
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setBirthdate(birthdate);
            u.setGuild(guild);
            u.setDescription(description);
            u.setRank(rank);
            u.setLangKey(langKey);
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
        LocalDate now = new LocalDate();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token ->{
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
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
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
        return Optional.ofNullable(userRepository.getOne(id));
    }
}
