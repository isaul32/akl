package fi.tite.akl.web.rest;

import fi.tite.akl.repository.ChallongeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/challonge")
public class ChallongeResource {

    @Autowired
    ChallongeRepository challongeRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<String> get() throws IOException {
        return ResponseEntity.ok(challongeRepository.getScorecard("akl", "2019A"));
    }
}
