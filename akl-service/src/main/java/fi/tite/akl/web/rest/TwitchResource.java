package fi.tite.akl.web.rest;

import fi.tite.akl.dto.twitch.TwitchDto;
import fi.tite.akl.repository.TwitchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@Slf4j
@RestController
@RequestMapping("/api/twitch")
public class TwitchResource {

    @Inject
    private TwitchRepository twitchRepository;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public TwitchDto getTwitchState() {
        log.debug("REST request to get TwitchDto state");
        return twitchRepository.getTwitchState();
    }
}
