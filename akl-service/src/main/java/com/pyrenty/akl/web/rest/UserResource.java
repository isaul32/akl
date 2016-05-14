package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Authority;
import com.pyrenty.akl.domain.user.User;
import com.pyrenty.akl.repository.AuthorityRepository;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.service.UserService;
import com.pyrenty.akl.web.rest.dto.UserBaseDTO;
import com.pyrenty.akl.web.rest.mapper.UserMapper;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserMapper userMapper;

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<User> getAll(@RequestParam(required = false) Optional<Integer> offset,
                             @RequestParam(required = false) Optional<Integer> limit) {
        if (offset.isPresent() && limit.isPresent()) {
            Pageable paging = PaginationUtil.generatePageRequest(offset.get(), limit.get());
            Page<User> page = userRepository.findAll(paging);
            return page.getContent();
        } else {
            return userRepository.findAll();
        }
    }

    @RequestMapping(value = "/users/count", method = RequestMethod.GET)
    @Timed
    public long getCount() {
        return userRepository.count();
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    @Timed
    void deleteUser(@PathVariable Long id) {
        User user = userRepository.findOne(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @RequestMapping(value = "/users/authorities", method = RequestMethod.GET, produces =  MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Timed
    List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }

    @RequestMapping(value = "/users/{id}/authorities", method = RequestMethod.POST, produces =  MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Timed
    ResponseEntity<Void> updateUserAuthorities(@PathVariable Long id, @RequestBody Set<Authority> authorities) {
        User user = userService.getUserWithAuthorities(id);
        user.setAuthorities(authorities);
        userRepository.save(user);

        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("authority", id.toString())).build();
    }

    @RequestMapping(value = "/users/{id}/authorities", method = RequestMethod.GET, produces =  MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Timed
    ResponseEntity<Set<Authority>> getUserAuthorities(@PathVariable Long id) {
        User user = userService.getUserWithAuthorities(id);
        if (user != null) {
            return ResponseEntity.ok(user.getAuthorities());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<UserBaseDTO> getUser(@PathVariable Long id) {
        log.debug("REST request to get UserDTO : {}", id);
        return userRepository.findOneById(id)
                .map(user -> userMapper.userToUserBaseDTO(user))
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
