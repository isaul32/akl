package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.twitch.Twitch;
import com.pyrenty.akl.repository.TwitchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/api/twitch")
public class TwitchResource {
    private final Logger log = LoggerFactory.getLogger(TwitchResource.class);

    @Inject
    private TwitchRepository twitchRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Twitch getTwitchState() {
        log.debug("REST request to get Twitch state");
        return twitchRepository.getTwitchState();
    }
}
