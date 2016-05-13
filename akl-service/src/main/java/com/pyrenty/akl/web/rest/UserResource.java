package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.user.User;
import com.pyrenty.akl.repository.UserRepository;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;


/**
 * REST controller for managing users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

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
    @Transactional
    @Timed
    void deleteUser(@PathVariable Long id) {
        User user = userRepository.findOne(id);
        if (user != null) {
            userRepository.delete(user);
        }
    }

    @RequestMapping(value = "/users/{login}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<User> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userRepository.findOneByLogin(login)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
