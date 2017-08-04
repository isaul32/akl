package com.pyrenty.akl.web.rest;

import com.pyrenty.akl.domain.Season;
import com.pyrenty.akl.repository.SeasonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/seasons")
public class SeasonResource {

    @Inject
    private SeasonRepository seasonRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Season>> getAll() {
        log.debug("REST request to get all seasons");

        return ResponseEntity.ok(seasonRepository.findAll());
    }

}
