package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Authority;
import com.pyrenty.akl.domain.User;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.service.UserService;
import com.pyrenty.akl.web.rest.dto.UserExtendedDTO;
import com.pyrenty.akl.web.rest.dto.UserPublicDTO;
import com.pyrenty.akl.web.rest.mapper.UserMapper;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api/users")
public class UserResource {
    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserMapper userMapper;

    @RequestMapping(value = "/public", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<UserPublicDTO>> getAllUsers(@RequestParam(value = "page", required = false) Integer offset,
                                                           @RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {

        Page<User> page = userService.getAllUsers(PaginationUtil.generatePageRequest(offset, limit, new Sort(
                new Sort.Order(Sort.Direction.ASC, "id")
        )));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);

        return new ResponseEntity<>(page.getContent().stream()
                .map(userMapper::userToUserPublicDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @Timed
    public ResponseEntity<List<UserExtendedDTO>> getAllExtendedUsers(@RequestParam(value = "page", required = false) Integer offset,
                                                                     @RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {

        Page<User> page = userService.getAllUsers(PaginationUtil.generatePageRequest(offset, limit, new Sort(
                new Sort.Order(Sort.Direction.ASC, "id")
        )));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);

        return new ResponseEntity<>(page.getContent().stream()
                .map(userMapper::userToUserExtendedDTO)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    @Timed
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("User deleted", id.toString())).build();
    }

    @RequestMapping(value = "/authorities", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @Timed
    public List<Authority> getAuthorities() {

        return userService.getAllAuthorities();
    }

    @RequestMapping(value = "/{id}/authorities", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @Timed
    public ResponseEntity<Void> updateUserAuthorities(@PathVariable Long id, @RequestBody Set<Authority> authorities) {
        userService.updateUserAuthorities(id, authorities);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Authorities updated", id.toString())).build();
    }

    @RequestMapping(value = "/{id}/authorities", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    @Timed
    public ResponseEntity<Set<Authority>> getUserAuthorities(@PathVariable Long id) {

        return Optional.ofNullable(userService.getUserWithAuthorities(id))
                .map(user -> ResponseEntity.ok(user.getAuthorities()))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Timed
    public ResponseEntity<UserPublicDTO> getUser(@PathVariable Long id) {
        log.debug("REST request to get UserBaseDto : {}", id);

        return userService.getUser(id)
                .map(userMapper::userToUserPublicDTO)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/steamid/{steamId}", method = RequestMethod.GET)
    @Timed
    ResponseEntity<String> getUserAuthorityBySteamId(@PathVariable String steamId) {
        return Optional.ofNullable(userService.getUserAuthorityBySteamId(steamId))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/communityid/{communityId}", method = RequestMethod.GET)
    @Timed
    ResponseEntity<String> getUserAuthorityByCommunityId(@PathVariable String communityId) {
        return Optional.ofNullable(userService.getUserAuthorityByCommunityId(communityId))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
