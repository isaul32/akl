package fi.tite.akl.web.rest;

import fi.tite.akl.domain.Authority;
import fi.tite.akl.domain.User;
import fi.tite.akl.dto.UserExtendedDto;
import fi.tite.akl.dto.UserPublicDto;
import fi.tite.akl.repository.UserRepository;
import fi.tite.akl.service.UserService;
import fi.tite.akl.web.rest.mapper.UserMapper;
import fi.tite.akl.web.rest.util.HeaderUtil;
import fi.tite.akl.web.rest.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Inject
    private UserService userService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserMapper userMapper;

    @RequestMapping(value = "/public", method = RequestMethod.GET)
    public ResponseEntity<List<UserPublicDto>> getAllUsers(@RequestParam(value = "page", required = false) Integer offset,
                                                           @RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {

        Page<User> page = userService.getAllUsers(PaginationUtil.generatePageRequest(offset, limit, new Sort(
                new Sort.Order(Sort.Direction.ASC, "id")
        )));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);

        return new ResponseEntity<>(page.getContent().stream()
                .map(userMapper::userToUserPublicDto)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserExtendedDto>> getAllExtendedUsers(@RequestParam(value = "page", required = false) Integer offset,
                                                                     @RequestParam(value = "per_page", required = false) Integer limit,
                                                                     @RequestParam(value = "filter", required = false, defaultValue = "") String filter) throws URISyntaxException {

        Pageable pageable = PaginationUtil.generatePageRequest(offset, limit, new Sort(
                new Sort.Order(Sort.Direction.ASC, "id")
        ));

        Page<User> page = userRepository.findByNicknameContainingIgnoreCase(filter, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);

        return new ResponseEntity<>(page.getContent().stream()
                .map(userMapper::userToUserExtendedDto)
                .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("User deleted", id.toString())).build();
    }

    @RequestMapping(value = "/authorities", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public List<Authority> getAuthorities() {

        return userService.getAllAuthorities();
    }

    @RequestMapping(value = "/{id}/authorities", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserAuthorities(@PathVariable Long id, @RequestBody Set<Authority> authorities) {
        userService.updateUserAuthorities(id, authorities);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("Authorities updated", id.toString())).build();
    }

    @RequestMapping(value = "/{id}/authorities", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<Authority>> getUserAuthorities(@PathVariable Long id) {

        return Optional.ofNullable(userService.getUserWithAuthorities(id))
                .map(user -> ResponseEntity.ok(user.getAuthorities()))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserPublicDto> getUser(@PathVariable Long id) {
        log.debug("REST request to get UserBaseDto : {}", id);

        return userService.getUser(id)
                .map(userMapper::userToUserPublicDto)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/steamid/{steamId}", method = RequestMethod.GET)
    ResponseEntity<Set<Authority>> getUserAuthorityBySteamId(@PathVariable String steamId) {
        return Optional.ofNullable(userService.getUserAuthorityBySteamId(steamId))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @RequestMapping(value = "/communityid/{communityId}", method = RequestMethod.GET)
    ResponseEntity<Set<Authority>> getUserAuthorityByCommunityId(@PathVariable String communityId) {
        return Optional.ofNullable(userService.getUserAuthorityByCommunityId(communityId))
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
