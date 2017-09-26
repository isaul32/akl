package fi.tite.akl.web.rest;

import fi.tite.akl.domain.PersistentToken;
import fi.tite.akl.domain.Season;
import fi.tite.akl.dto.KeyAndPasswordDto;
import fi.tite.akl.dto.TeamDto;
import fi.tite.akl.dto.UserDto;
import fi.tite.akl.repository.PersistentTokenRepository;
import fi.tite.akl.repository.SeasonRepository;
import fi.tite.akl.repository.TeamRepository;
import fi.tite.akl.repository.UserRepository;
import fi.tite.akl.security.SecurityUtils;
import fi.tite.akl.service.MailService;
import fi.tite.akl.service.UserService;
import fi.tite.akl.service.util.RandomUtil;
import fi.tite.akl.mapper.TeamMapper;
import fi.tite.akl.mapper.UserMapper;
import fi.tite.akl.web.rest.errors.CustomParameterizedException;
import fi.tite.akl.web.rest.util.HeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class AccountResource {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private TeamMapper teamMapper;

    @Inject
    private UserMapper userMapper;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private MailService mailService;

    @Inject
    private SeasonRepository seasonRepository;

    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public ResponseEntity<String> activateAccount(@RequestParam(value = "key") String key) {
        return userService.activateRegistration(key)
                .map(user -> new ResponseEntity<String>(HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET  /authenticate -> check if the user is authenticated, and return its login.
     */
    @RequestMapping(value = "/authenticate",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return ResponseEntity.ok(request.getRemoteUser());
    }

    /**
     * GET  /account -> get the current user.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
                .map(userMapper::userToUserDto)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET /account/team -> get the team of the current user
     */
    @RequestMapping(value = "/account/team",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeamDto> getTeam() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
                .map(user -> {
                    Season activeSeason = seasonRepository.findByArchived(false);
                    if (activeSeason == null) {
                        throw new CustomParameterizedException("Team doesn't belong in active season");
                    }

                    return teamRepository.findOneByMembersIdAndSeasonId(user.getId(), activeSeason.getId());
                })
                .map(teamMapper::teamToTeamDto)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * POST  /account -> update the current user information.
     */
    @RequestMapping(value = "/account",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveAccount(@RequestBody UserDto userDto, HttpServletRequest request) {
        if (SecurityUtils.isAuthenticated()) {
            return userRepository
                    .findOneByLogin(userDto.getLogin())
                    .filter(u -> u.getLogin().equals(SecurityUtils.getCurrentLogin()))
                    .map(u -> {
                        // Send activation message
                        if (!u.isActivated()) {
                            // Generate new key
                            u.setActivationKey(RandomUtil.generateActivationKey());
                            // Todo: Restrict user mail send

                            String baseUrl = request.getScheme()
                                    + "://"
                                    + request.getServerName()
                                    + ":"
                                    + request.getServerPort();
                            u.setEmail(userDto.getEmail());
                            mailService.sendActivationEmail(u, baseUrl);
                        }

                        return u;
                    })
                    .map(u -> {
                        userService.updateUserInformation(userDto.getNickname(), userDto.getFirstName(), userDto.getLastName(),
                                userDto.getEmail(), userDto.getBirthdate(), userDto.getGuild(),
                                userDto.getDescription(), userDto.getRank(), userDto.getLangKey(), u.getActivationKey());
                        return new ResponseEntity<String>(HttpStatus.OK);
                    })
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/account/change_login",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changeLogin(@RequestBody String login) {
        userService.changeLogin(login);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("Login name changed", login))
                .body(null);
    }

    @RequestMapping(value = "/account/sessions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
                .map(user -> new ResponseEntity<>(
                        persistentTokenRepository.findByUser(user),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @RequestMapping(value = "/account/sessions/{series}", method = RequestMethod.DELETE)
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(
                u -> persistentTokenRepository.findByUser(u).stream()
                        .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
                        .findAny().ifPresent(t -> persistentTokenRepository.delete(decodedSeries)));
    }

    @RequestMapping(value = "/account/reset_password/init",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> requestPasswordReset(@RequestBody String mail, HttpServletRequest request) {

        return userService.requestPasswordReset(mail)
                .map(user -> {
                    String baseUrl = request.getScheme() +
                            "://" +
                            request.getServerName() +
                            ":" +
                            request.getServerPort();
                    mailService.sendPasswordResetMail(user, baseUrl);
                    return new ResponseEntity<>("e-mail was sent", HttpStatus.OK);
                }).orElse(new ResponseEntity<>("e-mail address not registered", HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/account/reset_password/finish",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDto keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
                .map(user -> new ResponseEntity<String>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) && password.length() >= 5 && password.length() <= 100);
    }
}
