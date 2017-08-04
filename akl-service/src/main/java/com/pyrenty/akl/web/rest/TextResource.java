package com.pyrenty.akl.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.pyrenty.akl.domain.Text;
import com.pyrenty.akl.repository.TextRepository;
import com.pyrenty.akl.web.rest.util.HeaderUtil;
import com.pyrenty.akl.web.rest.util.PaginationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Text.
 */
@Slf4j
@RestController
@RequestMapping("/api/texts")
public class TextResource {

    @Inject
    private TextRepository textRepository;

    @Timed
    @PreAuthorize("hasRole('CMS') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Text>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                             @RequestParam(value = "per_page", required = false) Integer limit)
            throws URISyntaxException {
        log.debug("REST request to get all Texts");

        Page<Text> page = textRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @Timed
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Text> get(@PathVariable Long id) {
        log.debug("REST request to get Text : {}", id);

        return Optional.ofNullable(textRepository.findOne(id))
            .map(text -> new ResponseEntity<>(text, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Timed
    @PreAuthorize("hasRole('CMS') or hasRole('ADMIN')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Text> update(@PathVariable Long id,
                                       @RequestBody Text text) throws URISyntaxException {
        log.debug("REST request to update Text : {}", id);

        return Optional.ofNullable(textRepository.findOne(id))
                .map(t -> {
                    t.setFi(text.getFi());
                    t.setEn(text.getEn());
                    return textRepository.save(t);
                })
                .map(t -> ResponseEntity.ok()
                        .headers(HeaderUtil.createAlert("Text updated", id.toString()))
                        .body(t))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
