package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Authority;
import com.pyrenty.akl.domain.PersistentToken;
import com.pyrenty.akl.dto.UserDto;
import com.pyrenty.akl.repository.PersistentTokenRepository;
import com.pyrenty.akl.repository.TeamRepository;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.security.SecurityUtils;
import com.pyrenty.akl.service.MailService;
import com.pyrenty.akl.service.UserService;
import com.pyrenty.akl.service.util.RandomUtil;
import com.pyrenty.akl.dto.KeyAndPasswordDto;
import com.pyrenty.akl.dto.TeamDto;
import com.pyrenty.akl.web.rest.mapper.TeamMapper;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private TeamRepository teamRepository;

    @Inject
    private TeamMapper teamMapper;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private MailService mailService;

    // Normal register is not used
    /*@RequestMapping(value = "/register", method = RequestMethod.POST)
    @Timed
    public ResponseEntity<?> registerAccount(@Valid @RequestBody UserDto userDto, HttpServletRequest request) {
        return userRepository.findOneByLogin(userDto.getLogin())
            .map(user -> new ResponseEntity<>("login already in use", HttpStatus.BAD_REQUEST))
            .orElseGet(() -> userRepository.findOneByEmail(userDto.getEmail())
                .map(user -> new ResponseEntity<>("e-mail address already in use", HttpStatus.BAD_REQUEST))
                .orElseGet(() -> {
                    User user = userService.createUserInformation(userDto.getLogin(), userDto.getPassword(),
                    userDto.getFirstName(), userDto.getLastName(), userDto.getEmail().toLowerCase(),
                    userDto.getLangKey());
                    String baseUrl = request.getScheme() + // "http"
                    "://" +                                // "://"
                    request.getServerName() +              // "myhost"
                    ":" +                                  // ":"
                    request.getServerPort();               // "80"

                    mailService.sendActivationEmail(user, baseUrl);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                })
        );
    }*/

    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    @Timed
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
    @Timed
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
    @Timed
    public ResponseEntity<UserDto> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
                .map(user -> ResponseEntity.ok(new UserDto(
                        user.getId(),
                        user.getLogin(),
                        user.getNickname(),
                        null, // Password
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getBirthdate(),
                        user.getGuild(),
                        user.getDescription(),
                        user.getRank(),
                        user.isActivated(),
                        user.getCommunityId(),
                        user.getSteamId(),
                        user.getLangKey(),
                        user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toList()),
                        // Team ID
                        user.getCaptain() == null ? (
                                user.getMember() == null ? (
                                        user.getStandin() == null ? null : user.getStandin().getId()
                                ) : user.getMember().getId()
                        ) : user.getCaptain().getId(),
                        teamRepository.findOneForUser(user.getId()) != null
                )))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * GET /account/team -> get the team of the current user
     */
    @RequestMapping(value = "/account/team",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TeamDto> getTeam() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
                .map(user -> teamRepository.findOneForUser(user.getId()))
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
    @Timed
    public ResponseEntity<String> saveAccount(@RequestBody UserDto userDto, HttpServletRequest request) {
        return userRepository
            .findOneByLogin(userDto.getLogin())
            .filter(u -> u.getLogin().equals(SecurityUtils.getCurrentLogin()))
            .map(u -> {
                // Send activation message
                if (!u.isActivated()) {
                    // Generate new key
                    u.setActivationKey(RandomUtil.generateActivationKey());
                    // todo: Restrict user mail send

                    String baseUrl = request.getScheme() +         // "http"
                            "://" +                                // "://"
                            request.getServerName() +              // "myhost"
                            ":" +                                  // ":"
                            request.getServerPort();               // "80"
                    u.setEmail(userDto.getEmail());
                    mailService.sendActivationEmail(u, baseUrl);
                } else {
                    // Lock email
                    userDto.setEmail(u.getEmail());
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

    @RequestMapping(value = "/account/change_password",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
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
    @Timed
    public ResponseEntity<?> changeLogin(@RequestBody String login) {
        userService.changeLogin(login);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createAlert("Login name changed", login))
                .body(null);
    }

    @RequestMapping(value = "/account/sessions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<PersistentToken>> getCurrentSessions() {
        return userRepository.findOneByLogin(SecurityUtils.getCurrentLogin())
            .map(user -> new ResponseEntity<>(
                persistentTokenRepository.findByUser(user),
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * DELETE  /account/sessions?series={series} -> invalidate an existing session.
     *
     * - You can only delete your own sessions, not any other user's session
     * - If you delete one of your existing sessions, and that you are currently logged in on that session, you will
     *   still be able to use that session, until you quit your browser: it does not work in real time (there is
     *   no API for that), it only removes the "remember me" cookie
     * - This is also true if you invalidate your current session: you will still be able to use it until you close
     *   your browser or that the session times out. But automatic login (the "remember me" cookie) will not work
     *   anymore.
     *   There is an API to invalidate the current session, but there is no API to check which session uses which
     *   cookie.
     */
    @RequestMapping(value = "/account/sessions/{series}",
            method = RequestMethod.DELETE)
    @Timed
    public void invalidateSession(@PathVariable String series) throws UnsupportedEncodingException {
        String decodedSeries = URLDecoder.decode(series, "UTF-8");
        userRepository.findOneByLogin(SecurityUtils.getCurrentLogin()).ifPresent(
            u-> persistentTokenRepository.findByUser(u).stream()
            .filter(persistentToken -> StringUtils.equals(persistentToken.getSeries(), decodedSeries))
            .findAny().ifPresent(t -> persistentTokenRepository.delete(decodedSeries)));
    }

    @RequestMapping(value = "/account/reset_password/init",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
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
    @Timed
    public ResponseEntity<String> finishPasswordReset(@RequestBody KeyAndPasswordDto keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        return userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
              .map(user -> new ResponseEntity<String>(HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    private boolean checkPasswordLength(String password) {
      return (!StringUtils.isEmpty(password) && password.length() >= UserDto.PASSWORD_MIN_LENGTH && password.length() <= UserDto.PASSWORD_MAX_LENGTH);
    }

}
