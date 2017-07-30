package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Season;
import com.pyrenty.akl.repository.SeasonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/api/seasons")
public class SeasonResource {
    private final Logger log = LoggerFactory.getLogger(SeasonResource.class);

    @Inject
    private SeasonRepository seasonRepository;

    @RequestMapping(method = RequestMethod.GET)
    @Timed
    public ResponseEntity<List<Season>> getAll() {
        log.debug("REST request to get all seasons");

        return ResponseEntity.ok(seasonRepository.findAll());
    }

}
